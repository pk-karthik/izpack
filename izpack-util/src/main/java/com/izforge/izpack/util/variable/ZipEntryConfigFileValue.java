package com.izforge.izpack.util.variable;

import java.io.InputStream;
import java.util.zip.*;

import com.izforge.izpack.api.substitutor.SubstitutionType;
import com.izforge.izpack.util.substitutor.VariableSubstitutorBase;


public class ZipEntryConfigFileValue extends ConfigFileValue
{

    private String filename;
    private String entryname;

    public ZipEntryConfigFileValue(String filename, String entryname, int type, String section, String key)
    {
        super(type, section, key);
        this.filename = filename;
        this.entryname = entryname;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getEntryname()
    {
        return entryname;
    }

    public void setEntryname(String entryname)
    {
        this.entryname = entryname;
    }

    @Override
    public void validate() throws Exception
    {
        super.validate();
        if (this.filename == null || this.filename.length() <= 0)
            throw new Exception("No or empty file path given to read entry from");
        if (this.entryname == null || this.entryname.length() <= 0)
            throw new Exception("No or empty file entry given to read entry from");
    }

    @Override
    public String resolve() throws Exception
    {
        return super.resolve(getZipEntryInputStream(getFilename(), getEntryname()));
    }

    @Override
    public String resolve(VariableSubstitutorBase... substitutors)
    throws Exception
    {
        String _filename_ = this.filename, _entryname_ = this.entryname;
        for ( VariableSubstitutorBase substitutor : substitutors )
            _filename_ = substitutor.substitute(_filename_, (SubstitutionType)null);
        for ( VariableSubstitutorBase substitutor : substitutors )
            _entryname_ = substitutor.substitute(_entryname_, (SubstitutionType)null);
        return super.resolve(getZipEntryInputStream(_filename_, _entryname_), substitutors);
    }

    private InputStream getZipEntryInputStream( String filename, String entryname ) throws Exception {
        ZipFile zipfile;
        try {
            zipfile = new ZipFile(filename);
            ZipEntry entry = zipfile.getEntry(entryname);
            if (entry == null)
                throw new Exception("Zip file entry "+entryname+" not found in "+zipfile.getName());
            return zipfile.getInputStream(entry);
        }
        catch (ZipException ze)
        {
            throw new Exception("Error opening zip file "+filename, ze);
        }
    }
}
