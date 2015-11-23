#!/bin/sh
#$ -S /bin/sh
#$ -N nwen303_test
#$ -wd /vol/grid-solar/sgeusers/chendifu
#$ -pe nwen303_1.pe 4
#
echo ==UNAME==
uname -n
/usr/pkg/bin/mpirun -np $NSLOTS \
 /usr/pkg/java/sun-7/bin/java   \
   -classpath /u/students/chendifu/Desktop/NWEN\ 303/Project2 \
 Sort med.3.killer.1000.txt insertion
