/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package homework03

import com.soywiz.korio.file.VfsOpenMode
import com.soywiz.korio.file.std.localVfs
import com.soywiz.korio.stream.writeString
import homework03.client.RedditClient
import homework03.csv.csvSerialize
import homework03.model.TopicSnapshot
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class App {

    val greeting: String
        get() {
            return "Hello world"
        }
}

fun main(args: Array<String>): Unit = runBlocking {
    val redditClient = RedditClient()

    val topics = mutableListOf<TopicSnapshot>()
    for (topicName in args) {
        topics.add(async { redditClient.getTopic(topicName) }.await())
    }
    val topicsCsv = csvSerialize(topics, TopicSnapshot::class)
    write(topicsCsv, "/Users/aleksa30/logs", "--subjects.csv")
}


suspend fun write(csv: String, path: String, filename: String) {
    val fileVfs = localVfs(path)
    fileVfs[filename].delete()
    val file = fileVfs[filename].open(VfsOpenMode.CREATE)
    file.writeString(csv)
    file.close()
}