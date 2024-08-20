package it.luke.plsql.main;

import it.luke.neo4j.APP;
import it.luke.plsql.pojo.PlSqlParserTree;
import it.luke.plsql.pojo.TableInfo;
import it.luke.plsql.visitor.PlSqlGraphVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CharStream;

import java.io.IOException;
import java.util.Properties;

public class PlSqlGraph {

    private static Properties props = new Properties();

    /**
     * Processa e analisa o arquivo PL/SQL.
     *
     * @param fileName o nome do arquivo a ser processado
     * @throws IOException se ocorrer um erro de I/O
     */
    public static void processFile(String fileName) throws IOException {
        PlSqlParserTree tree = null;
        String resultName = "t_res_log_0130";

        System.out.println("Start " + fileName);

        // Usar CharStreams para ler o arquivo
        CharStream input = CharStreams.fromFileName(fileName);
        tree = new PlSqlParserTree(input);

        PlSqlGraphVisitor visitor = new PlSqlGraphVisitor(resultName, tree);
        // Começar a visitar os nós da árvore
        visitor.visit();

        TableInfo mainTable = visitor.mainTable;
        APP.addDepGraphWithObj(mainTable);

        System.out.println("End");
    }

    public static void main(String[] args) {
        String fileName = "testData/plsql.sql";
        if (fileName != null && !fileName.isEmpty()) {
            try {
                processFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No files to parse");
        }
    }
}
