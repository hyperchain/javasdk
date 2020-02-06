package cn.hyperchain.sdk.common.utils;

import cn.hyperchain.sdk.crypto.HashUtil;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import java.io.StringReader;

public class CertUtil {

    /**
     * read pem and convert to address.
     * @param s pem file context
     * @return address
     * @throws Exception -
     */
    public static String pemToAddr(String s) throws Exception {
        PemReader pemReader = new PemReader(new StringReader(s));
        PemObject pemObject = pemReader.readPemObject();
        X509CertificateHolder cert = new X509CertificateHolder(pemObject.getContent());
        SubjectPublicKeyInfo pkInfo = cert.getSubjectPublicKeyInfo();
        DERBitString pk = pkInfo.getPublicKeyData();
        byte[] pk64 = ByteUtils.subArray(pk.getBytes(),1);
        return ByteUtils.toHexString(HashUtil.sha3omit12(pk64));
    }
}
