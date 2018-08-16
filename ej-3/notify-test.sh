while true
do
    make
    if [ $? -ne 0 ]; 
    then
        paplay /usr/share/sounds/freedesktop/stereo/bell.oga 2> /dev/null
    else
        paplay /usr/share/sounds/freedesktop/stereo/complete.oga 2> /dev/null
    fi
    inotifywait -e modify $(find -name "*.[chx]")
done
