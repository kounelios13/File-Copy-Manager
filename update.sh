#!usr/bin/bash
echo "Give a message to commit"
read "msg"
git add "*.*"
git commit -m"$msg"
git push
echo "Press enter to exit:"
read "dummy"