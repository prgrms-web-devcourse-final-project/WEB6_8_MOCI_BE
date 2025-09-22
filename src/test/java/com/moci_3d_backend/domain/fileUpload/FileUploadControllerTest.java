package com.moci_3d_backend.domain.fileUpload;

import com.moci_3d_backend.domain.fileUpload.controller.FileUploadController;
import com.moci_3d_backend.domain.fileUpload.repository.FileUploadRepository;
import com.moci_3d_backend.domain.fileUpload.service.FileUploadService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FileUploadController.class)
@Transactional
public class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileUploadService fileUploadService;

    @MockBean
    private FileUploadRepository fileUploadRepository;

    @Test
    @DisplayName("파일 업로드 테스트")
    void t1() throws Exception {
        MockMultipartFile mockFile =
                new MockMultipartFile("uploadFile", "testFile.txt", "text/plain", "test".getBytes());
        mockMvc.perform(multipart("/file")
                        .file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string("파일 업로드 완료"));
    }
}
