/*
 * Institute For Systems Genetics. NYU Langone HealthCopyright (c) 2021. Created by Sergei German
 */

package org.nyumc.isg.lims.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.nyumc.isg.lims.common.Jsonable;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("SpellCheckingInspection")
public class BAC implements Jsonable {
    private String chromosome;
    private String bin;
    private final int start;
    private final int end;
    private final String name;
    private int score;
    private final String strand;
    private int thickStart;
    private int thickEnd;
    private String reserved;
    private int blockCount;
    private String blockSize;
    private String chromStarts;

    /**
     * Depricated constructor
     * @param bin - bin id
     * @param start - start coordinate
     * @param end - end coordinate
     * @param name - name
     * @param score - score value
     * @param strand - strand (direction)
     * @param thickStart - thick start coordinate
     * @param thickEnd - thick end coordinate
     * @param reserved - reserved
     * @param blockCount - block count
     * @param blockSize - block size
     * @param chromStarts chromosome coordinate
     */
    @Deprecated
    public BAC(String bin,
               int start, int end,
               String name, int score, String strand,
               int thickStart, int thickEnd, String reserved,
               int blockCount, String blockSize, String chromStarts) {
        this.bin = bin;
        this.start = start;
        this.end = end;
        this.name = name;
        this.score = score;
        this.strand = strand;
        this.thickStart = thickStart;
        this.thickEnd = thickEnd;
        this.reserved = reserved;
        this.blockCount = blockCount;
        this.blockSize = blockSize;
        this.chromStarts = chromStarts;
    }

    /**
     *
     * @param name - bac name
     * @param chromosome - chromosome
     * @param start - start coordinagte
     * @param end - end coordinate
     * @param strand - strand (direction)
     */
    public BAC(String name, String chromosome, int start, int end, String strand) {
        this.name = name;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.strand = strand;
    }

    /**
     *
     * @param rs - SQL result set
     * @throws SQLException - SQL exception
     */
    public BAC(ResultSet rs) throws SQLException {
        this.chromosome = rs.getString("chromosome");
        this.bin = rs.getString("bin");
        this.start = rs.getInt("start_loc");
        this.end = rs.getInt("end_loc");
        this.name = rs.getString("name");
        this.score = rs.getInt("score");
        this.strand = rs.getString("strand");
        this.thickStart = rs.getInt("thick_start");
        this.thickEnd = rs.getInt("thick_end");
        this.reserved = rs.getString("reserved");
        this.blockCount = rs.getInt("block_count");
        this.blockSize = rs.getString("block_size");
        this.chromStarts = rs.getString("chrom_starts");
    }

    /**
     *
     * @return - csv representation of the BAC
     */
    public String toCSV(){
        return name + "," + chromosome + "," + start+ "," + end + "," + strand;
    }

    /**
     *
     * @return chromosome
     */
    public String getChromosome() {
        return chromosome;
    }

    /**
     *
     * @return bin
     */
    public String getBin() {
        return bin;
    }

    /**
     *
     * @return - start coordinatye
     */
    public int getStart() {
        return start;
    }

    /**
     *
     * @return - end coordinate
     */
    public int getEnd() {
        return end;
    }

    /**
     *
     * @return - bac name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return bac score
     */
    public int getScore() {
        return score;
    }

    /**
     *
     * @return bac strand
     */
    public String getStrand() {
        return strand;
    }

    /**
     *
     * @return - thick start
     */
    public int getThickStart() {
        return thickStart;
    }

    /**
     *
     * @return thick end
     */
    public int getThickEnd() {
        return thickEnd;
    }

    /**
     *
     * @return reserved value
     */
    public String getReserved() {
        return reserved;
    }

    /**
     *
     * @return block count
     */
    public int getBlockCount() {
        return blockCount;
    }

    /**
     *
     * @return block size
     */
    public String getBlockSize() {
        return blockSize;
    }

    /**
     *
     * @return chromosome start
     */
    public String getChromStarts() {
        return chromStarts;
    }

    /**
     *
     * @return BED format representation
     */
    public String toBEDString() {
        return "BAC{" +
                "chromosome='" + chromosome + '\'' +
                ", bin='" + bin + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", strand='" + strand + '\'' +
                ", thickStart=" + thickStart +
                ", thickEnd=" + thickEnd +
                ", reserved='" + reserved + '\'' +
                ", blockCount=" + blockCount +
                ", blockSize='" + blockSize + '\'' +
                ", chromStarts='" + chromStarts + '\'' +
                '}';
    }

    /**
     *
     * @param other other BAC object
     * @return true if the objects have the same name, false otherwise
     */
    @Override
    public boolean equals(Object other){
        if (other.getClass().equals(this.getClass()))
            return this.name.equals(((BAC)other).name);
        return false;
    }

    /**
     *
     * @return json object representation of the BAC object
     */
    public JsonObject toJsonObject() {
        JsonObject jo = new JsonObject();
        jo.add("name", new JsonPrimitive(name));
        jo.add("chromosome", new JsonPrimitive(chromosome));
        jo.add("start", new JsonPrimitive(start));
        jo.add("end", new JsonPrimitive(end));
        jo.add("strand", new JsonPrimitive(strand));
        return jo;
    }

    /**
     *
     * @return json string representation of the BAC object
     */
    public String toJson() {
        return toJsonObject().toString();
    }

    /**
     *
     * @return object as json string
     */
    @Override
    public String toString() {
        return toJson();
    }
}
