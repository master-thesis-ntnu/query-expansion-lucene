import json

def readLinesFromFile(inputFile, outputFile):
    x = 0
    line = inputFile.readline()
    while line:
        flickrDataResult = json.loads(line)
        if not 'photo' in flickrDataResult:
            line = inputFile.readline()
            continue
        photo = flickrDataResult['photo']

        # Parse photo object to change format
        formatTagsAttribute(photo)
        formatTitleAttribute(photo)
        formatDescriptionAttribute(photo)
        formatUrlAttribute(photo)

        # Write parsed photo object to file
        outputFile.write(json.dumps(photo) + '\n')

        line = inputFile.readline()

        if x % 10000 == 0:
            print 'Parsed photos so far: ' + str(x)
        x += 1

    inputFile.close()
    outputFile.close()

def formatTagsAttribute(photo):
    photo['tags'] = photo['tags']['tag']

    for tag in photo['tags']:
        tag['content'] = tag['_content']
        tag.pop('_content')

def formatTitleAttribute(photo):
    photo['title'] = photo['title']['_content']

def formatDescriptionAttribute(photo):
    photo['description'] = photo['description']['_content']

def formatUrlAttribute(photo):
    photo['urls'] = photo['urls']['url']

    for url in photo['urls']:
        url['content'] = url['_content']
        url.pop('_content')

def main():
    inputFile = open('../data/flickr.data')
    outputFile = open('../data/flickr-parsed.data', 'w')
    readLinesFromFile(inputFile, outputFile)

main()
