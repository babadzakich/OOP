package ru.nsu.chuvashov;

import com.example.parser.configurationLexer;
import com.example.parser.configurationParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import ru.nsu.chuvashov.configholder.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

public class MyParser {
    public static Configuration parseConfig() throws IOException {
        String configContent = readFile("configurations.dsl");
        configurationLexer lexer = new configurationLexer(CharStreams.fromString(configContent));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        configurationParser parser = new configurationParser(tokens);

        ParseTree tree = parser.program();
        MyVisitor visitor = new MyVisitor();
        visitor.visit(tree);

        Set<String> imports = visitor.getImports();

        for (String importFile : imports) {
            String content = readFile(importFile);
            lexer = new configurationLexer(CharStreams.fromString(content));
            tokens = new CommonTokenStream(lexer);
            parser = new configurationParser(tokens);
            tree = parser.program();
            visitor.visit(tree);
        }

        return new Configuration(
                visitor.getTools(),
                visitor.getGroups(),
                visitor.getTasks(),
                visitor.getStudentCheckers(),
                visitor.getControlPoints(),
                new ArrayList<>(visitor.getImports())
        );
    }

    private static String readFile(String fileName) throws IOException {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new IOException("Resource file '" + fileName + "' not found");
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
