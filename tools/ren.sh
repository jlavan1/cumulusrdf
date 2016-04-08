for name in *.owl
do
    newname="$(echo "$name" | cut -c5-)"
    mv "$name" "$newname"
done
