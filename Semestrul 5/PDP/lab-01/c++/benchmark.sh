#!/bin/bash
# Usage: .\benchmark.sh ".\MainSequentialStatic" 10
# Usage: .\benchmark.sh ".\MainSequentialDynamic" 10
# Usage: .\benchmark.sh ".\MainThreadsStatic 2" 10
# Usage: .\benchmark.sh ".\MainThreadsDynamic 2" 10

COMMAND="$1"
COUNT="$2"

sum=0

for ((i=1; i<=COUNT; i++))
do
    clear
    echo "Running iteration $i of $COUNT..."
    output=$($COMMAND)

    if echo "$output" | grep -q "Warning: result differs from previous run!"; then
        echo "Warning: result differs from previous run!"
		sleep 7
        exit 1
    fi

    time_line=$(echo "$output" | grep "Time")
    time=$(echo "$time_line" | grep -oP '\d+')
    sum=$((sum + time))
done

average=$((sum / COUNT))
echo
echo "All runs completed."
echo "Average time: $average ms"
sleep 1000000000
