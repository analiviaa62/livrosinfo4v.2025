package br.edu.ifrn.livros.util;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class FileUploadUtil {

    public static void salvarArquivo(String diretorioUpload, String nomeArquivo, MultipartFile multipartFile) throws IOException {
        Path caminhoUpload = Paths.get(diretorioUpload);

        if (!Files.exists(caminhoUpload)) {
            Files.createDirectories(caminhoUpload);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path caminhoArquivo = caminhoUpload.resolve(nomeArquivo);
            // Substitui o arquivo se ele já existir
            Files.copy(inputStream, caminhoArquivo, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Não foi possível salvar o arquivo: " + nomeArquivo, ioe);
        }
    }
}