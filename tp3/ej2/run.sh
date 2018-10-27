#!/bin/bash

trap ctrl_c int

function ctrl_c() {
    PID=$(ps aux | grep -i "proxy.py" | head -1 | cut -d " " -f3)
    if [ ! $PID ]; then
        PID=$(ps aux | grep -i "proxy.py" | head -1 | cut -d " " -f4)
    fi
    echo "CTRL + C trapped, killing server [PID $PID]"
    kill $PID
}

./proxy.py &
npm --prefix static/js run build && env python3 -m http.server --cgi 8081
