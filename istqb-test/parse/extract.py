def cfilename(i):
    return "questions/" + str(i) + "-" + str(i + 9) + ".txt"

def read(src):
    f = open(src,'r')
    filedata = f.read()
    f.close()
    return filedata

def write(dest, content):
    f = open(dest,'w')
    f.write(content)
    f.close()

def writeanswers(dest, answers):
    f = open(dest,'w')
    for i in range(1, 1061):
        f.write("Q." + str(i) + ": " + answers[i] + "\n")
    f.close()

def extractanswers(content):
    lines = content.splitlines()
    answers = {}
    for line in lines:
        if (line.startswith("| Q.")):
            num = int(line[5:10].strip())
            answer = line[41:42]
            answers[num] = answer
    return answers

def convert(content):
    result = ""
    lines = content.splitlines()
    state = "none"
    for line in lines:
        if (line.startswith("Q. ")):
            state = "question"
        if (line.startswith("Correct Answer of the above Questions")):
            state = "end"
        if (line.startswith("Correct Answer of All above Questions")):
            state = "end"
        if (line.startswith("CORRECT ANSWERS TO ABOVE QUESTIONS")):
            state = "end"
        if (line == "<<<<<< =================== >>>>>>"):
            continue
        if (state == "question"):
            line = line.replace("\xa0", "")
            line = line.replace("\xc2", "")
            if not(line.startswith("|")):
                line = " ".join(line.split())
            result = result + line + "\n"
    return result

def mergedicts(x, y):
    z = x.copy()   # start with x's keys and values
    z.update(y)    # modifies z with y's keys and values & returns None
    return z

i = 1
allanswers = {}
allcontent = ""
while (i <= 1051):
    filename = cfilename(i)
    content = read(filename)
    converted = convert(content)
    allcontent = allcontent + converted
    answers = extractanswers(content)
    allanswers = mergedicts(allanswers, answers)
    i = i + 10

# print(allanswers)
write("questions.txt", allcontent)
writeanswers("answers.txt", allanswers)
