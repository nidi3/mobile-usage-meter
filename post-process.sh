jarsigner -sigalg SHA1withRSA -digestalg SHA1 -keystore $2 -storepass $3 $1-unsigned.apk mykey
rm $1.apk
zipalign -v 4 $1-unsigned.apk $1.apk
