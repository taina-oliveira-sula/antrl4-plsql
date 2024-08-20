package it.luke.plsql.pojo;

import it.luke.antlr.generated.PlSqlLexer;
import it.luke.antlr.generated.PlSqlParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * PlSqlRuleVisitor
 */
public class PlSqlParserTree {

    private CharStream input;
    private PlSqlLexer lexer;
    private CommonTokenStream tokens;
    private PlSqlParser parser;
    private TokenStreamRewriter rewriter;
    private ParseTree parseTree;

    public CharStream getInput(CharStream input) {
        return input;
    }

    public CharStream getInput() {
        return input;
    }

    public TokenStreamRewriter getRewriter() {
        return rewriter;
    }

    public ParseTree getParseTree() {
        return parseTree;
    }

    public PlSqlParserTree(CharStream input) {
        this.input = input; // Agora usando CharStream
        parseInput(); // Realizando a inicialização
    }

    // Inicialização
    private ParseTree parseInput() {
        // Criando o lexer
        lexer = new PlSqlLexer(input);
        lexer.removeErrorListeners();
        // Construindo o fluxo de tokens a partir do lexer
        tokens = new CommonTokenStream(lexer);
        // Criando o parser
        parser = new PlSqlParser(tokens);
        rewriter = new TokenStreamRewriter(tokens);
        // Gerando a árvore de análise a partir do parser
        parseTree = parser.compilation_unit();

        return parseTree;
    }

    public String getResultText() {
        return rewriter.getText();
    }
}
