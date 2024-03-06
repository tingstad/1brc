#!/bin/bash
set -e

if [ $# -lt 3 ]; then
    echo "Map Reduce wc"
    echo ""
    echo "Usage: $0 FILE JOBS COMMAND..."
    echo ""
    echo "Example: $0 big.txt 8 wc"
    echo "Or: $0 big.txt 8 awk '{ lines++; words+=NF; bytes+=length(\$0)+1 } END{ print lines,words,bytes}'"
    exit
fi

file="$1"; procs="$2"; shift 2

[ -e "$file" ] || {
    echo >&2 "Not found: $file"
    exit 1; }

[ "$procs" -gt 0 ] || {
    echo >&2 "Jobs must be >0 but was: $procs"
    exit 1; }

size=$(ls -lkng "$file" | awk '{print $4}')
megs=$(( size / 1024 / 1024 ))

ps=$(awk "BEGIN{ for(i=0; i<$procs; i++) s = (s \" \" i); print s;}")

declare -a start byteoffs adjustment
len=$(( megs / procs ))

for i in $ps; do
    offset=$(( i * len ))
    start[$i]=$offset
    [ $i -gt 0 ] || continue
    offset=$(( offset * 1048576 ))
    adjustment[$i]=$(dd if="$file" bs=1 skip=$offset \
      | head -n1 | wc -c | awk '{print $1}')
    byteoffs[$i]=$(( $offset + 0 ))
done
adjustment[0]=0
byteoffs[0]=0

job() (
    i=$1; res="$2"; shift 2

    [ $i -lt $((procs-1)) ] && count="count=$((len))" || count=""

    (
        dd if="$file" bs=1024k skip=${start[$i]} $count 2>/dev/null \
        | (
            dd bs=1 of=/dev/null count=${adjustment[$i]} 2>/dev/null
            cat
        )
        if [ $i -lt $((procs-1)) ]; then
            dd if="$file" bs=1 skip=${byteoffs[$((i+1))]} \
              count=${adjustment[$((i+1))]} 2>/dev/null
        fi
    ) | "$@" >"$res"
)

mkfifo pipe guard
>pipe <guard &
for i in $ps; do
    job $i pipe "$@" &
done

lines=0 words=0 bytes=0
for _i in $ps; do
    read n w b
    lines=$(( lines + n ))
    words=$(( words + w ))
    bytes=$(( bytes + b ))
done <pipe

>guard; wait
rm guard pipe

printf "%d %d %d %s\n" $lines $words $bytes "$file"

