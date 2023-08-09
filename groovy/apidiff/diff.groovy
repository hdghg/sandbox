#!/usr/bin/env groovy
import groovy.json.JsonSlurper

def parseGeneral(File file) {
    file.readLines()
            .findAll { !it.startsWithAny("#", "\t") }
            .findAll { !it.isEmpty() }
            .findAll { !it.contains("- root -") }
            .collect { it =~ /.*\s(\/\S+)\s-\s(\w+).*/ }
            .collect {"${it[0][2]} ${it[0][1]}" as String}
            .unique()
}

def slurper = new JsonSlurper()
def swagger = slurper.parse(new File("swagger.json"))

def swaggerUrls = []
for (pathObj in swagger.paths) {
    for (methodObj in pathObj.value) {
        swaggerUrls += "${methodObj.key.toUpperCase()} $pathObj.key".toString()
    }
}
println "size: " + swaggerUrls.size()

def sourceAudio = parseGeneral(new File("source_txt/general_httpreq_audio.txt"))
def sourceVideo = parseGeneral(new File("source_txt/general_httpreq_video.txt"))
println "size: " + sourceAudio.size()
println "size: " + sourceVideo.size()

def resultAudio = []
def resultVideo = []
for (String line : sourceAudio) {
    if (line in swaggerUrls) {
        resultAudio += line
    }
}
for (String line : sourceVideo) {
    if (line in swaggerUrls) {
        resultVideo += line
    }
}

for (def line : resultAudio) {
    swaggerUrls -= line
}
for (def line : resultVideo) {
    swaggerUrls -= line
}

def comparator = {a,b -> a.split(" ")[1] <=> b.split(" ")[1] ?: a.split(" ")[0] <=> b.split(" ")[0]}
swaggerUrls.sort comparator
resultAudio.sort comparator
resultVideo.sort comparator

new File("result_txt/audio.txt").write(resultAudio.join("\n"))
new File("result_txt/video.txt").write(resultVideo.join("\n"))
new File("result_txt/unused.txt").write(swaggerUrls.join("\n"))
