package ru.yandex.practicum.filmorate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {

	@Autowired
	private FilmController filmController;

	@Autowired
	private UserController userController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() throws Exception {
		assertThat(filmController).isNotNull();
		assertThat(userController).isNotNull();
	}

	@Test
	public void testsForUserController() throws Exception {
		String userJsonNull = "";
		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJsonNull))
				.andExpect(status().is4xxClientError());

		String userJson = "{\n" +
				"  \"login\": \"ann\",\n" +
				"  \"name\": \"Some Name\",\n" +
				"  \"email\": \"abc@mail.ru\",\n" +
				"  \"birthday\": \"1980-07-30\"\n" +
				"}";
		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson))
				.andExpect(status().isOk())
				.andExpect((content().string(containsString("ann"))))
				.andExpect((content().string(containsString("Some Name"))))
				.andExpect((content().string(containsString("abc@mail.ru"))))
				.andExpect((content().string(containsString("1980-07-30"))));

		String userJson1 = "{\n" +
				"  \"id\": 1,\n" +
				"  \"login\": \"annaaaa\",\n" +
				"  \"name\": \"Some Name\",\n" +
				"  \"email\": \"abc@mail.ru\",\n" +
				"  \"birthday\": \"1980-07-30\"\n" +
				"}";
		this.mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJson1))
						.andExpect(status().isOk());

		String userJsonWrongId = "{\n" +
				"  \"id\": 1425,\n" +
				"  \"login\": \"grig\",\n" +
				"  \"name\": \"next Name\",\n" +
				"  \"email\": \"rrr@mail.ru\",\n" +
				"  \"birthday\": \"1990-27-10\"\n" +
				"}";
		this.mockMvc.perform(put("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJsonWrongId))
				.andExpect(status().is4xxClientError());

		String userJsonWrongLogin = "{\n" +
				"  \"login\": \"grig jjj\",\n" +
				"  \"name\": \"Gr Name\",\n" +
				"  \"email\": \"hghgg@mail.ru\",\n" +
				"  \"birthday\": \"1995-02-15\"\n" +
				"}";
		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJsonWrongLogin))
				.andExpect(status().is5xxServerError());

		String userJsonEmptyName = "{\n" +
				"  \"login\": \"vital\",\n" +
				"  \"name\": \"\",\n" +
				"  \"email\": \"vvvg@mail.ru\",\n" +
				"  \"birthday\": \"2000-01-11\"\n" +
				"}";
		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJsonEmptyName))
				.andExpect(status().isOk());

		String userJsonWrongEmail = "{\n" +
				"  \"login\": \"timur\",\n" +
				"  \"name\": \"TName\",\n" +
				"  \"email\": \"hghggjhjkj\",\n" +
				"  \"birthday\": \"1980-22-17\"\n" +
				"}";
		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJsonWrongEmail))
				.andExpect(status().is4xxClientError());

		String userJsonBirthdayInFuture = "{\n" +
				"  \"login\": \"nastya\",\n" +
				"  \"name\": \"anastasia\",\n" +
				"  \"email\": \"hhh@fjh.dj\",\n" +
				"  \"birthday\": \"2100-10-19\"\n" +
				"}";
		this.mockMvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(userJsonBirthdayInFuture))
				.andExpect(status().is5xxServerError());

	}

	@Test
	public void testsForFilmController() throws Exception {
		String filmJsonNull = "";
		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJsonNull))
				.andExpect(status().is4xxClientError());

		String filmJson = "{\n" +
				"  \"name\": \"Happyness\",\n" +
				"  \"description\": \"Something good\",\n" +
				"  \"releaseDate\": \"1991-03-15\",\n" +
				"  \"duration\": 90\n" +
				"}";
		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJson))
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJson))
				.andExpect(status().isOk())
				.andExpect((content().string(containsString("Happyness"))))
				.andExpect((content().string(containsString("Something good"))))
				.andExpect((content().string(containsString("90"))))
				.andExpect((content().string(containsString("1991-03-15"))));

		String filmJson1 = "{\n" +
				"  \"id\": 1,\n" +
				"  \"name\": \"Happyness\",\n" +
				"  \"description\": \"Something good\",\n" +
				"  \"releaseDate\": \"1990-03-15\",\n" +
				"  \"duration\": 90\n" +
				"}";
		this.mockMvc.perform(put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJson1))
				.andExpect(status().isOk());

		String filmJsonWrongId = "{\n" +
				"  \"id\": 247,\n" +
				"  \"name\": \"hjyyt\",\n" +
				"  \"description\": \"fuytfghv\",\n" +
				"  \"releaseDate\": \"1998-06-25\",\n" +
				"  \"duration\": 190\n" +
				"}";
		this.mockMvc.perform(put("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJsonWrongId))
				.andExpect(status().is5xxServerError());

		String filmJsonEmptyName = "{\n" +
				"  \"name\": \"\",\n" +
				"  \"description\": \"?\",\n" +
				"  \"releaseDate\": \"1968-02-19\",\n" +
				"  \"duration\": 105\n" +
				"}";

		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJsonEmptyName))
				.andExpect(status().is5xxServerError());

		String filmJsonLongDescription = "{\n" +
				"  \"name\": \"Titanic\",\n" +
				"  \"description\": \"Very long description Very long description Very long description " +
				"Very long description Very long description Very long description Very long description " +
				"Very long description Very long description Very long description Very long description\",\n" +
				"  \"releaseDate\": \"1900-10-12\",\n" +
				"  \"duration\": 120\n" +
				"}";
		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJsonLongDescription))
				.andExpect(status().is5xxServerError());


		String filmJsonWrongReleaseDate = "{\n" +
				"  \"name\": \"Flower\",\n" +
				"  \"description\": \"About the Nature\",\n" +
				"  \"releaseDate\": \"685-09-29\",\n" +
				"  \"duration\": 15\n" +
				"}";
		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJsonWrongReleaseDate))
				.andExpect(status().is4xxClientError());

		String filmJsonNegativeDuration = "{\n" +
				"  \"name\": \"House\",\n" +
				"  \"description\": \"About house\",\n" +
				"  \"releaseDate\": \"1988-07-17\",\n" +
				"  \"duration\": -152\n" +
				"}";
		this.mockMvc.perform(post("/films")
						.contentType(MediaType.APPLICATION_JSON)
						.content(filmJsonNegativeDuration))
				.andExpect(status().is5xxServerError());
	}
}
