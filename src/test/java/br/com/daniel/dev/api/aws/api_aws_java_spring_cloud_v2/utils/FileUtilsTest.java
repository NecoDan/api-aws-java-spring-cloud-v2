package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

//    @Test
//    void generateCsvFileCreatesFileWithCorrectContent() throws Exception {
//        String filePath = "test.csv";
//        List<String[]> data = List.of(new String[]{"Header1", "Header2"}, new String[]{"Value1", "Value2"});
//
//        FileUtils.generateCsvFile(filePath, data);
//
//        assertTrue(Files.exists(Path.of(filePath)));
//        assertEquals("Header1,Header2\nValue1,Value2\n", Files.readString(Path.of(filePath)));
//        Files.delete(Path.of(filePath));
//    }

//    @Test
//    void generateCsvFileThrowsExceptionForInvalidPath() {
//        String invalidPath = "/invalid/path/test.csv";
//        List<String[]> data = (List<String[]>) Arrays.asList(new String[]{"Header1", "Header2"});
//
//        assertThrows(FileSystemException.class, () -> FileUtils.generateCsvFile(invalidPath, data));
//    }

//    @Test
//    void generateCsvFileReturnsCorrectByteArray() throws Exception {
//        List<String[]> data = List.of(new String[]{"Header1", "Header2"}, new String[]{"Value1", "Value2"});
//
//        byte[] result = FileUtils.generateCsvFile(data);
//
//        assertNotNull(result);
//        assertEquals("Header1,Header2\nValue1,Value2\n", new String(result));
//    }

    @Test
    void convertStringToFileCreatesMultipartFileWithCorrectContent() throws IOException {
        String content = "Sample content";
        String fileName = "sample.txt";
        String contentType = "text/plain";

        MockMultipartFile multipartFile = (MockMultipartFile) FileUtils.convertStringToFile(content, fileName, contentType);

        assertEquals(fileName, multipartFile.getOriginalFilename());
        assertEquals(contentType, multipartFile.getContentType());
        assertEquals(content, new String(multipartFile.getBytes()));
    }

    @Test
    void createFromFilePathCreatesMultipartFileFromValidPath() throws Exception {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.writeString(tempFile, "Sample content");

        MockMultipartFile multipartFile = (MockMultipartFile) FileUtils.createFromFilePath(tempFile.toString());

        assertEquals(tempFile.getFileName().toString(), multipartFile.getOriginalFilename());
        assertEquals("Sample content", new String(multipartFile.getBytes()));
        Files.delete(tempFile);
    }

    @Test
    void createFolderIfNotExistsCreatesFolderWhenItDoesNotExist() {
        Path tempDir = Path.of("tempFolder");
        Path result = FileUtils.createFolderIfNotExists("", "tempFolder");

        assertTrue(Files.exists(result));
        assertTrue(Files.isDirectory(result));
        assertEquals(tempDir.toAbsolutePath(), result);

        tempDir.toFile().delete();
    }

    @Test
    void deleteFolderRemovesFileSuccessfully() throws Exception {
        Path tempFile = Files.createTempFile("test", ".txt");

        FileUtils.deleteFolder(tempFile.toString());

        assertFalse(Files.exists(tempFile));
    }

    @Test
    void getFileNameListReturnsCorrectFileNames() throws Exception {
        Path tempDir = Files.createTempDirectory("testDir");
        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.txt"));

        List<String> fileNames = FileUtils.getFileNameList(tempDir.toString());

        assertTrue(fileNames.contains("file1.txt"));
        assertTrue(fileNames.contains("file2.txt"));

        Files.deleteIfExists(tempDir.resolve("file1.txt"));
        Files.deleteIfExists(tempDir.resolve("file2.txt"));
        Files.delete(tempDir);
    }
}