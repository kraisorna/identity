package com.kraisorn.identitykotlinspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
class IdentityKotlinSpringApplication

fun main(args: Array<String>) {
	runApplication<IdentityKotlinSpringApplication>(*args)
}

@RestController
@RequestMapping("/api/v1")
class MessageResource(val service: MessageService) {
	@GetMapping("/name")
	fun indexName(@RequestParam("name") name: String) = "Hello, $name!"

	@GetMapping("/all")
	fun indexAll(@RequestParam("all") all: String) = listOf(
		Message("1", "Hello!"),
		Message("2", "Bonjour!"),
		Message("3", "Privet!"),
	)

	@GetMapping("/")
	fun index(): List<Message> = service.findMessages()

	@GetMapping("/{id}")
	fun indexId(@PathVariable id: String): List<Message> =
		service.findMessageById(id)

	@PostMapping("/")
	fun post(@RequestBody message: Message) {
		service.post(message)
	}
}

interface MessageRepository : CrudRepository<Message, String> {

	@Query("select * from messages")
	fun findMessages(): List<Message>
}

@Table("MESSAGES")
//@Table("messages")
data class Message(@Id val id: String?, val text: String)

@Service
class MessageService(val db: MessageRepository) {

	fun findMessages(): List<Message> = db.findMessages()

	fun findMessageById(id: String): List<Message> = db.findById(id).toList()

	fun post(message: Message){
		db.save(message)
	}

	fun <T : Any> Optional<out T>.toList(): List<T> =
		if (isPresent) listOf(get()) else emptyList()
}