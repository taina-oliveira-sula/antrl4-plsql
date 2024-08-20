package it.luke.antlr.generated;

import it.luke.antlr.generated.myListener.MyPlSqlListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Classe principal para executar o analisador PL/SQL usando ANTLR
 * @author luke
 */
public class PlsqlMain {

    public static void run(String expr) {
        // Construir um fluxo de caracteres a partir da expressão de entrada
        var input = CharStreams.fromString(expr);

        // Usar o fluxo de caracteres para construir o lexer
        PlSqlLexer lexer = new PlSqlLexer(input);

        // Criar um fluxo de tokens a partir do lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Criar o parser a partir do fluxo de tokens
        PlSqlParser parser = new PlSqlParser(tokens);

        // Criar o walker para percorrer a árvore de análise
        ParseTreeWalker walker = new ParseTreeWalker();

        // Executar o listener personalizado sobre a árvore de análise
        walker.walk(new MyPlSqlListener(), parser.selected_tableview());
    }

    public static void main(String[] args) {
        run("select a from table;");
    }
}
