package jck.com.jck;

import org.springframework.web.bind.annotation.*;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.UUID;
import java.util.regex.*;

@RestController
@RequestMapping("/jck compiler")
public class JckCompiler {

    @PostMapping
    public String compileAndRun(@RequestBody String code) throws IOException {
        // Extract original class name
        String oldClassName = extractClassName(code);
        if (oldClassName == null) {
            return "No class found in code!";
        }

        // Generate random class name
        String newClassName = "Main" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        String fileName = newClassName + ".java";

        // Replace class name in code
        code = code.replaceFirst("class\\s+" + oldClassName, "class " + newClassName);

        // Write code to file
        FileWriter writer = new FileWriter(fileName);
        writer.write(code);
        writer.close();

        // Compile file
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null, fileName);
        if (result != 0) {
            return "Compilation failed";
        }

        // Run the compiled class
        ProcessBuilder pb = new ProcessBuilder("java", newClassName);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        return output.toString();
    }

    // ✅ Helper method to extract class name using regex
    private String extractClassName(String code) {
        Pattern pattern = Pattern.compile("class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
