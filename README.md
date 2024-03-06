# 1brc

## wc

```
$ time wc measurements.txt
 1000000000 1179189219 13795419106 measurements.txt

real    0m50.841s
user    0m43.175s
sys     0m5.535s
```

```
$ time parallel -a measurements.txt --pipepart --jobs 8 --block -1 wc \
| awk '{ n+=$1; w+=$2; b+=$3 } END{ print n,w,b }'
1000000000 1179189219 13795419106

real    0m17.458s
user    1m18.953s
sys     0m26.449s
```

```
$ time ./mapreducewc.sh measurements.txt 8 wc
1000000000 1179189219 13795419106 measurements.txt

real    0m17.759s
user    1m20.504s
sys     0m23.331s
```

```
$ time ./mapreducewc.sh measurements.txt 8 awk \
'{ lines++; words+=NF; bytes+=length($0)+1 } END{ print lines,words,bytes}'
1000000000 1179189219 13795419106 measurements.txt

real    4m30.303s
user    33m18.292s
sys     0m32.818s
```

```
$ time head -n 1000000 measurements.txt | wc
 1000000 1179654 13797206

real    0m0.131s
user    0m0.158s
sys     0m0.016s
```

```
$ time head -n 1000000 measurements.txt | awk \
'{ lines++; words+=NF; bytes+=length($0)+1 } END{ print lines,words,bytes}'
1000000 1179654 13797206

real    0m0.984s
user    0m1.080s
sys     0m0.013s
```

```
$ time head -n 1000000 measurements.txt | clojure -X brc/main
1000000 1179654 13797206

real    0m5.697s
user    0m7.789s
sys     0m0.607s
```