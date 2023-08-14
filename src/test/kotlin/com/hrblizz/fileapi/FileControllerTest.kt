package com.hrblizz.fileapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.hrblizz.fileapi.data.entities.File
import com.hrblizz.fileapi.data.entities.FileMetasReq
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    val basicAuthValue = "Basic YWRtaW46aHVudGVyMg==";
    val file = File(
        "token-1",
        "token-test-name",
        "txt",
        mapOf("meta-item-1" to "meta-item-1-value"),
        mapOf("source-item-1" to "source-item-1-value"),
        null,
        Date(),
        "test-file-content"
    )

    @Test
    fun `Should have no access`() {
        mockMvc.post("/files") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(file)
        }
            .andDo { print() }
            .andExpect {
                status { isUnauthorized() }
            }
    }



    @Test
    fun `POST & GET & DELETE file`() {
        mockMvc.post("/files") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(file)
            header("Authorization", basicAuthValue)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("data.token") {
                    value(file.token)
                }
            }

        mockMvc.get("/file/${file.token}") {
            header("Authorization", basicAuthValue)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { file.content }
            }

        mockMvc.delete("/file/${file.token}") {
            header("Authorization", basicAuthValue)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun `GET files metas`() {
        mockMvc.post("/files") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(file)
            header("Authorization", basicAuthValue)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("data.token") {
                    value(file.token)
                }
            }

        val fileMetaReq = FileMetasReq(arrayListOf(file.token))
        mockMvc.post("/files/metas") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(fileMetaReq)
            header("Authorization", basicAuthValue)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("data.files.${file.token}.filename") {
                    value(file.name)
                }
                jsonPath("data.files.${file.token}.size") {
                    value(file.content.length)
                }
            }

        mockMvc.delete("/file/${file.token}") {
            header("Authorization", basicAuthValue)
        }
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }


}
