import urllib2
from subprocess import call

def downloadFile(url, filename):

    print(url)

    f = urllib2.urlopen(url)
    with open(filename, "wb") as file:
        file.write(f.read())
        file.write("ssw")
    print("Got it")

def cfilename(i):
    return str(i) + "-" + str(i + 9) + ".html"

def cconvertedfilename(i):
    return str(i) + "-" + str(i + 9) + "_utf8.html"

def cstrongremovedfilename(i):
    return str(i) + "-" + str(i + 9) + "_wostrong.html"

def ctxtfilename(i):
    return str(i) + "-" + str(i + 9) + ".txt"

def curl(i):
    filename = "istqb-certification-exam-sample-papers-q-" + str(i) + "-to-" + str(i + 9)
    prefix = "http://www.softwaretestinggenius.com/"
    url = prefix + filename
    return url

def convert(src, dest):
    call(["iconv", "-f", "ISO-8859-1", "-t", "UTF-8", src, "-o", dest])

def removestrong(src, dest):
    f = open(src,'r')
    filedata = f.read()
    f.close()

    filedata = filedata.replace("<strong>","")
    filedata = filedata.replace("<STRONG>","")
    filedata = filedata.replace("</strong>","")
    filedata = filedata.replace("</STRONG>","")

    f = open(dest,'w')
    f.write(filedata)
    f.close()

def totxt(src, dest):
    call(["pandoc", src, "-o", dest, "-t", "plain"])

i = 1
while (i <= 1051):
    print("Converting " + str(i))
    filename = cfilename(i)
    url = curl(i)
    convertedfilename = cconvertedfilename(i)
    downloadFile(url, filename)
    convert(filename, convertedfilename)
    strongremovedfilename = cstrongremovedfilename(i)
    removestrong(convertedfilename, strongremovedfilename)
    txtfilename = ctxtfilename(i)
    totxt(strongremovedfilename, txtfilename)
    i = i + 10
