#!/bin/bash

trap ctrl_c int

function ctrl_c() {
    PID=$(ps aux | grep -i "proxy.py" | cut -d " " -f3 | head -1)
    echo "CTRL + C trapped, killing server [PID $PID]"
    kill $PID
}

./proxy.py &
env python3 -m http.server --cgi 8081
