package edu.jhu.isi.grothsahai.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Statement;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.StatementAndWitness;
import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;
import java.util.List;

public class Serializer {
    private static GsonBuilder gsonBuilder = new GsonBuilder();

    public static String serializeCRS(final CommonReferenceString crs) {
        return null;
    }

    public static CommonReferenceString deserializeCRS(final String crs) {
        return null;
    }

    public static String serializeStatementAndWitness(final StatementAndWitness statementWitnessPair, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.toJson(statementWitnessPair);
    }

    public static StatementAndWitness deserializeStatementAndWitness(final String statementWitness, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.fromJson(statementWitness, StatementAndWitness.class);
    }

    public static String serializeStatement(final List<Statement> statements, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        final Statements statementsObj = new Statements(statements);
        return gson.toJson(statementsObj);
    }

    public static List<Statement> deserializeStatement(final String statement, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.fromJson(statement, Statements.class).getStatements();
    }

    private static class Statements {
        private List<Statement> statements = new ArrayList<>();

        public Statements() {
        }

        Statements(final List<Statement> statements) {
            this.statements = statements;
        }

        List<Statement> getStatements() {
            return statements;
        }
    }

    public static String serializeWitness(final Witness witness, final CommonReferenceString crs) {
        final Gson gson = gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create();
        return gson.toJson(witness);
    }

    public static Witness deserializeWitness(final String witness, final CommonReferenceString crs) {
        return gsonBuilder
                .registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .fromJson(witness, Witness.class);
    }

    public static String serializeProof(final Proof proof, final CommonReferenceString crs) {
        return gsonBuilder.registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .toJson(proof);
    }

    public static Proof deserializeProof(final String proof, final CommonReferenceString crs) {
        return gsonBuilder
                .registerTypeAdapter(Element.class, new ElementJsonSerializer(crs))
                .create()
                .fromJson(proof, Proof.class);
    }
}
