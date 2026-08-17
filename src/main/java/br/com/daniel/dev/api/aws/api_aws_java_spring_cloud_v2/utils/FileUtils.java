package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2.utils;

import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Classe utilitária para manipulação de arquivos.
 * <p>
 * <p>
 * Esta classe fornece métodos para geração de arquivos CSV, conversão de conteúdo
 * para `MultipartFile` e outras operações relacionadas a arquivos.
 *
 * <p>Esta classe não pode ser instanciada.</p>
 */
public final class FileUtils {

    private FileUtils() {
        throw new IllegalStateException("This is a utility class FileUtils and cannot be instantiated");
    }

    /**
     * Gera um arquivo CSV no caminho especificado a partir de uma lista de dados.
     *
     * @param filePath O caminho onde o arquivo CSV será gerado.
     * @param data     A lista de arrays de strings que representam as linhas e colunas do arquivo CSV.
     * @throws FileSystemException Se ocorrer um erro ao gerar o arquivo CSV.
     */
    public static void generateCsvFile(String filePath, List<String[]> data) throws FileSystemException {

        try (var csvWriter = new CSVWriter(new FileWriter(filePath))) {
            csvWriter.writeAll(data);
        } catch (IOException e) {
            throw new FileSystemException("Error while generating CSV file: " + e.getMessage());
        }
    }

    /**
     * Gera um arquivo CSV em formato de array de bytes a partir de uma lista de dados.
     *
     * @param data A lista de arrays de strings que representam as linhas e colunas do arquivo CSV.
     * @return Um array de bytes contendo o conteúdo do arquivo CSV gerado.
     * @throws FileSystemException Se ocorrer um erro ao gerar o conteúdo do arquivo CSV.
     */
    public static byte[] generateCsvFile(List<String[]> data) throws FileSystemException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); var csvWriter = new CSVWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            csvWriter.writeAll(data);
            csvWriter.flush();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new FileSystemException("Error while generating CSV content: " + e.getMessage());
        }
    }

    /**
     * Create a `MultipartFile` from a string content, file name, and content type.
     *
     * @param content     The content of the file as a string.
     * @param fileName    The name of the file (e.g., "data.json" or "document.txt").
     * @param contentType The MIME type of the file (e.g., "application/json" or "text/plain").
     * @return A `MultipartFile` object representing the file.
     */
    public static MultipartFile convertStringToFile(String content, String fileName, String contentType) {

        // Conversão da string para bytes utilizando encode UTF-8
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        return createFromBytes(contentBytes, fileName, contentType);
    }

    /**
     * Cria um objeto `MultipartFile` a partir de um caminho de arquivo.
     *
     * @param filePathStr O caminho do arquivo como uma string.
     * @return Um objeto `MultipartFile` representando o arquivo.
     * @throws IOException Se ocorrer um erro ao ler o arquivo.
     */
    public static MultipartFile createFromFilePath(String filePathStr) throws IOException {
        var path = Paths.get(filePathStr);
        final var originalFileName = path.getFileName().toString();
        final var contentType = Files.probeContentType(path); // Detectar automaticamente o tipo de conteúdo

        byte[] content = Files.readAllBytes(path);
        return createFromBytes(content, originalFileName, contentType);
    }

    /**
     * Cria um objeto `MultipartFile` a partir de um array de bytes.
     *
     * @param contentBytes O conteúdo do arquivo em formato de array de bytes.
     * @param fileName     O nome do arquivo original (por exemplo, "data.json" ou "document.txt").
     * @param contentType  O tipo de conteúdo MIME (por exemplo, "application/json" ou "text/plain").
     * @return Um objeto `MultipartFile` representando o arquivo.
     */
    public static MultipartFile createFromBytes(byte[] contentBytes, String fileName, String contentType) {
        return new MockMultipartFile("file",      // O nome do campo do formulário (nome do parâmetro)
                fileName,          // O nome do arquivo original (por exemplo, "data.json" ou "document.txt")
                contentType,       // O tipo de conteúdo (por exemplo, "application/json" ou "text/plain")
                contentBytes       // O conteúdo real do array de bytes
        );
    }

    /**
     * Obtém o caminho da pasta de recursos padrão do projeto.
     * <p>
     * Este método tenta localizar a pasta de recursos padrão utilizando o classloader
     * do contexto atual. Caso a pasta não seja encontrada, uma exceção será lançada.
     *
     * @return O caminho da pasta de recursos padrão como um objeto `Path`.
     * @throws IllegalStateException Se a pasta de recursos não for encontrada.
     */
    public static Path getDefaultResourcesFolderPath() {
        try {
            var classLoader = Thread.currentThread().getContextClassLoader();
            var resourceUrl = classLoader.getResource(StringUtils.EMPTY);

            if (resourceUrl != null) {
                Path resourcePath = Paths.get(resourceUrl.getPath());
                System.out.println("Default resources folder: " + resourcePath.toAbsolutePath());
                return resourcePath;
            }
            System.err.println("Resources folder not found.");
            throw new IllegalStateException("Failed/error Resources folder not found.");
        } catch (Exception e) {
            System.err.println("Resources folder not found.");
            throw new IllegalStateException("Resources folder not found: " + e.getMessage());
        }
    }

    /**
     * Obtém uma lista com os nomes dos arquivos em um diretório especificado.
     *
     * @param pathName O caminho do diretório onde os nomes dos arquivos serão listados.
     * @return Uma lista de strings contendo os nomes dos arquivos no diretório.
     */
    public static List<String> getFileNameList(String pathName) {
        var fileNameList = new ArrayList<String>();
        var directoryPath = Paths.get(pathName);

        try (Stream<Path> paths = Files.list(directoryPath)) {
            paths.forEach(path -> fileNameList.add(path.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
            throw new IllegalStateException("Error reading directory: " + e.getMessage());
        }

        return fileNameList;
    }

    /**
     * Cria uma pasta no caminho especificado, caso ela não exista.
     *
     * @param destinationFolderSaveFile O caminho do diretório onde a nova pasta será criada.
     * @param newFolderName             O nome da nova pasta a ser criada.
     * @return O caminho absoluto da pasta criada ou existente.
     */
    public static Path createFolderIfNotExists(String destinationFolderSaveFile, String newFolderName) {

        var folderPath = Paths.get(destinationFolderSaveFile + newFolderName);
        var absolutePath = folderPath.toAbsolutePath();

        if (!Files.exists(absolutePath)) {
            System.out.println("Folder does not exist: " + absolutePath);
            if (folderPath.toFile().mkdir() && Files.exists(absolutePath) && Files.isDirectory(absolutePath)) {
                System.out.println("Folder found at: " + absolutePath);
            }
        }

        return absolutePath;
    }

    /**
     * Exclui uma pasta ou arquivo no caminho especificado.
     *
     * @param pathFileName O caminho completo do arquivo ou pasta a ser excluído.
     *                     Deve ser um caminho válido e acessível no sistema de arquivos.
     */
    public static void deleteFolder(String pathFileName) {
        try {
            var filePath = Paths.get(pathFileName);
            Files.delete(filePath);
            System.out.println("File deleted successfully: " + filePath);
        } catch (IOException e) {
            System.err.println("Error deleting file: " + e.getMessage());
        }
    }
}
