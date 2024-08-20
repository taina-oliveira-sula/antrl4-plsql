package it.luke.neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Result;
import org.neo4j.driver.Transaction;
import it.luke.plsql.pojo.TableInfo;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;

public class APP {

    // Defina o driver como uma variável de instância da classe APP
    private static Driver driver;

    public static void main(String[] args) {
        // Inicialize o driver na variável de instância
        driver = GraphDatabase.driver("neo4j+s://c0c45999.databases.neo4j.io", AuthTokens.basic("neo4j", "_4BjkgBqwFY1rd1ytzJdmaSaG_T4R9EltlCgaAHfIII"));

        // Iniciar uma sessão
        try (Session session = driver.session()) {

            // Executar uma transação
            try (Transaction tx = session.beginTransaction()) {
                Result result = tx.run("CREATE (a:Person {name: $name, title: $title}) RETURN a.name AS name, a.title AS title",
                    parameters("name", "Tai", "title", "Developer"));

                // Iterar sobre os resultados
                while (result.hasNext()) {
                    System.out.println(result.next().get("name").asString());
                }

                // Confirmar a transação
                tx.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fechar o driver
            driver.close();
        }
    }

    public static void addDepGraphWithObj(TableInfo tableInfo) {
        Session session = getConnect();
        // Definir o nó principal (tabela de destino)
        String defaultLabel = "tables"; // Nó padrão
        String tableName = tableInfo.getTableName();
        StringBuilder sb = new StringBuilder();

        // Inicializar a criação do nó principal
        sb.append("CREATE (" + tableName + ":" + defaultLabel + " {comment:'" + tableName + "', status:'live'})\n");

        // Inicializar a criação da relação com o nó principal
        String recyp = "-[:join]->(" + tableName + "),\n";
        StringBuilder relationsb = new StringBuilder();
        relationsb.append("CREATE\n");

        ArrayList<TableInfo> joinClaus = tableInfo.getJoinClaus();
        // Verificar se há relações de associação
        if (joinClaus.size() > 0) {
            relationsb = relation_main_dep(recyp, relationsb, joinClaus);
            String substring = relationsb.substring(0, relationsb.length() - 2);
            sb.append(substring);
        }

        // Executar a declaração Cypher
        session.run(sb.toString());

        // Fechar recursos
        session.close();
        driver.close(); // Aqui a variável driver agora está acessível
    }

    // Método auxiliar para conectar ao Neo4j (certifique-se de implementar ou ajustar conforme necessário)
    private static Session getConnect() {
        driver = GraphDatabase.driver("neo4j+s://c0c45999.databases.neo4j.io", AuthTokens.basic("neo4j", "_4BjkgBqwFY1rd1ytzJdmaSaG_T4R9EltlCgaAHfIII"));
        return driver.session();
    }

    /**
     * Analisar o objeto e construir a declaração Cypher com relações associadas
     * @param recyp
     * @param relationsb
     * @param joinClaus
     * @return
     */
    public static StringBuilder relation_main_dep(String recyp, StringBuilder relationsb, ArrayList<TableInfo> joinClaus) {
        String defaultLabel = "tables";
        if (joinClaus.size() == 0) {
            return relationsb;
        }
        for (TableInfo tableInfo : joinClaus) {
            String tableName = tableInfo.getTableName();
            relationsb.append("CREATE (" + tableName + ":" + defaultLabel + " {comment:'table:" + tableName + "', status:'live'})\n");

            if (tableInfo.getJoinClaus().size() == 0) {
                relationsb.append("(" + tableName + ")" + recyp);
            } else {
                relationsb.append("(" + tableName + ")" + recyp);
                String recypSub = "-[:join]->(" + tableName + "),\n";
                relation_main_dep(recypSub, relationsb, tableInfo.getJoinClaus());
            }
        }
        return relationsb;
    }
}
