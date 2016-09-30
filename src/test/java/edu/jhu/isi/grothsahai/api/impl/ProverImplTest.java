package edu.jhu.isi.grothsahai.api.impl;

import edu.jhu.isi.grothsahai.BaseTest;
import edu.jhu.isi.grothsahai.api.Prover;
import edu.jhu.isi.grothsahai.entities.impl.CommonReferenceStringImpl;
import edu.jhu.isi.grothsahai.entities.impl.StatementAndWitness;
import edu.jhu.isi.grothsahai.entities.impl.ProofImpl;
import edu.jhu.isi.grothsahai.entities.impl.WitnessImpl;
import edu.jhu.isi.grothsahai.enums.Role;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticElement;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.springframework.util.Assert.notNull;

public class ProverImplTest extends BaseTest {
    @Test
    public void testProof() {
        final Pairing pairing = createPairing();
        final CommonReferenceStringImpl crs = CommonReferenceStringImpl.generate(pairing);
        final Prover prover = new ProverImpl(crs);
        final StatementAndWitness statementWitnessPair = new GeneratorImpl(Role.PROVER).generateStatementAndWitness(pairing);
        final WitnessImpl witness = (WitnessImpl) statementWitnessPair.getWitness();
        final ProofImpl proof = (ProofImpl) prover.proof(statementWitnessPair.getStatement(), witness);

        notNull(proof.getC());
        assertEquals(crs.getG1(), ((QuadraticElement) proof.getC().get(0)).getField().getTargetField());
        assertEquals(witness.getX().getLength(), proof.getC().getLength());
        notNull(proof.getD());
        assertEquals(crs.getG2(), ((QuadraticElement) proof.getD().get(0)).getField().getTargetField());
        assertEquals(witness.getY().getLength(), proof.getD().getLength());
        notNull(proof.getProofs().get(0).getPi());
        assertEquals(crs.getG2(), ((QuadraticElement) proof.getProofs().get(0).getPi().get(0)).getField().getTargetField());
        assertEquals(2, proof.getProofs().get(0).getPi().getLength());
        notNull(proof.getProofs().get(0).getTheta());
        assertEquals(crs.getG1(), ((QuadraticElement) proof.getProofs().get(0).getTheta().get(0)).getField().getTargetField());
        assertEquals(2, proof.getProofs().get(0).getTheta().getLength());
    }
}
