package com.kayode.ifypm.model;
import com.pgvector.PGvector;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

@Converter
public class VectorConverter implements AttributeConverter<float[], PGobject> {

    @Override
    public PGobject convertToDatabaseColumn(float[] vector) {
        if (vector == null) return null;
        try {
            PGvector pgVector = new PGvector(vector);
            PGobject obj = new PGobject();
            obj.setType("vector");
            obj.setValue(pgVector.toString());
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Error converting float[] to vector", e);
        }
    }

    @Override
    public float[] convertToEntityAttribute(PGobject obj) {
        if (obj == null || obj.getValue() == null) return null;
        try {
            PGvector pgVector = new PGvector(obj.getValue());
            return pgVector.toArray();
        } catch (Exception e) {
            throw new RuntimeException("Error converting vector to float[]", e);
        }
    }
}