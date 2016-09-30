package edu.jhu.isi.grothsahai.json;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Generator;
import edu.jhu.isi.grothsahai.api.NIZKFactory;
import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.api.Verifier;
import edu.jhu.isi.grothsahai.entities.CommonReferenceString;
import edu.jhu.isi.grothsahai.entities.Proof;
import edu.jhu.isi.grothsahai.entities.Witness;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.StatementImpl;
import edu.jhu.isi.grothsahai.entities.impl.WitnessImpl;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Pairing;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.util.Assert.isTrue;

public class SerializationTest extends BaseTest {
    @Test
    public void measureStatementSerialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);

        Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 2, 2, 1);

        final FileOutputStream fileWriter = new FileOutputStream("testStatement.json");
        final String statement = Serializer.serializeStatement(statementWitnessPair.getStatement(), (CommonReferenceStringImpl) crs);
        fileWriter.write(statement.getBytes());
        fileWriter.close();

        System.out.println("Size of Statement: " + Files.size(Paths.get("testStatement.json")) + " Bytes");
        Files.delete(Paths.get("testStatement.json"));
    }

    @Test
    public void measureProofSerialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        Prover prover = NIZKFactory.createProver(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 1);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());

        final FileOutputStream fileWriter = new FileOutputStream("testProof.json");
        fileWriter.write(Serializer.serializeProof((ProofImpl) proof, (CommonReferenceStringImpl) crs).getBytes());
        fileWriter.close();

        System.out.println("Size of proof: " + Files.size(Paths.get("testProof.json")) + " Bytes");
        Files.delete(Paths.get("testProof.json"));
    }

    @Test
    public void testStatementDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);

        Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 2, 2, 1);

        final FileOutputStream fileWriter = new FileOutputStream("testStatement.json");
        final String statement = Serializer.serializeStatement(statementWitnessPair.getStatement(), (CommonReferenceStringImpl) crs);
        fileWriter.write(statement.getBytes());
        fileWriter.close();


        final FileInputStream fileReader = new FileInputStream("testStatement.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final List<StatementImpl> statements = Serializer.deserializeStatement(new String(bytes), (CommonReferenceStringImpl) crs);

        Files.delete(Paths.get("testStatement.json"));
        for (int i = 0; i < statements.size(); i++) {
            assertEquals(statements.get(i), statementWitnessPair.getStatement().get(i));
        }
    }

    @Test
    public void testProofDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);
        Prover prover = NIZKFactory.createProver(crs);
        Verifier verifier = NIZKFactory.createVerifier(crs);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 1);
        final Proof proof = prover.proof(statementWitnessPair.getStatement(), statementWitnessPair.getWitness());

        final FileOutputStream fileWriter = new FileOutputStream("testProof.json");
        fileWriter.write(Serializer.serializeProof((ProofImpl) proof, (CommonReferenceStringImpl) crs).getBytes());
        fileWriter.close();

        final FileInputStream fileReader = new FileInputStream("testProof.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final Proof deserializedProof = Serializer.deserializeProof(new String(bytes), (CommonReferenceStringImpl) crs);
        Files.delete(Paths.get("testProof.json"));

        isTrue(verifier.verify(statementWitnessPair.getStatement(), deserializedProof));

    }

    @Test
    public void testWitnessDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 1);

        final FileOutputStream fileWriter = new FileOutputStream("testWitness.json");
        fileWriter.write(Serializer.serializeWitness((WitnessImpl) statementWitnessPair.getWitness(), (CommonReferenceStringImpl) crs).getBytes());
        fileWriter.close();

        final FileInputStream fileReader = new FileInputStream("testWitness.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final Witness witness = Serializer.deserializeWitness(new String(bytes), (CommonReferenceStringImpl) crs);
        Files.delete(Paths.get("testWitness.json"));

        assertEquals(statementWitnessPair.getWitness(), witness);
    }

    @Test
    @Ignore
    public void testStatementWitnessDeserialization() throws Exception {
        Generator generator = NIZKFactory.createGenerator(Role.PROVER);
        Pairing pairing = generator.generatePairing();
        final CommonReferenceString crs = generator.generateCRS(pairing);

        final StatementAndWitness statementWitnessPair = generator.generateStatementAndWitness(pairing, 1, 1, 1);

        final FileOutputStream fileWriter = new FileOutputStream("testStatementWitness.json");
        fileWriter.write(Serializer.serializeStatementAndWitness(statementWitnessPair, (CommonReferenceStringImpl) crs).getBytes());
        fileWriter.close();

        final FileInputStream fileReader = new FileInputStream("testStatementWitness.json");
        final int size = fileReader.available();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = (byte) fileReader.read();
        }

        final StatementAndWitness statementAndWitness = Serializer.deserializeStatementAndWitness(new String(bytes), (CommonReferenceStringImpl) crs);
        Files.delete(Paths.get("testStatementWitness.json"));

        assertEquals(statementWitnessPair, statementAndWitness);
    }
}
