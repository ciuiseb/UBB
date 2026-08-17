#!/bin/bash

if [ -f "sequential.cpp" ]; then
    g++ sequential.cpp -o sec -O2 > /dev/null 2>&1
    rm -f common/result.txt
    ./sec > /dev/null 2>&1
fi

HAS_STD=0
if [ -f "std" ]; then
    HAS_STD=1
fi


run_bench() {
    EXE=$1
    NP_FLAG=$2
    RUNS=$3
    TOTAL_TIME=0

    CMD="mpirun --allow-run-as-root --oversubscribe $NP_FLAG ./$EXE"

    for ((i=1; i<=RUNS; i++)); do
        OUTPUT=$($CMD)

        if echo "$OUTPUT" | grep -q "FAILED"; then
            echo "Executable:   ./$EXE"
            echo "Processes:    $NP_FLAG"
            echo "Run Number:   $i"
            echo "Reason:       Validation Failed"
            exit 1
        fi

        TIME=$(echo "$OUTPUT" | grep "Time:" | awk '{print $2}')

        TOTAL_TIME=$(python3 -c "print($TOTAL_TIME + $TIME)")
    done

    AVG=$(python3 -c "print('{:.2f}'.format($TOTAL_TIME / $RUNS))")

    NP_VAL=$(echo "$NP_FLAG" | awk '{print $2}')
    SUMMARY+="$EXE\t$NP_VAL\t\t$AVG ms\n"
}

SUMMARY="Executable\tProcesses\tAvg Time\n"
SUMMARY+="----------------------------------------\n"

RUNS=10

if [ $HAS_STD -eq 1 ]; then
    run_bench "std" "-np 5" $RUNS
    run_bench "std" "-np 9" $RUNS
    run_bench "std" "-np 17" $RUNS
fi

run_bench "sctgt" "-np 4" $RUNS
run_bench "sctgt" "-np 8" $RUNS
run_bench "sctgt" "-np 16" $RUNS

echo -e "$SUMMARY"