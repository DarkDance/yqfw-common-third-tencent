package cn.jzyunqi.common.utils;

import com.drew.imaging.FileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

class FileUtilPlusTest {

    @Test
    public void serviceAlgorithmsTest() throws Exception {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("e:\\vscodeworkspace\\testreadimg.h"));

        FileMagic fileMagic = FileUtilPlus.Files.typeOf(bufferedInputStream);
        System.out.println("文件类型(FileMagic)为：" + fileMagic);
        Assertions.assertEquals(FileMagic.JPEG, fileMagic);

        FileType fileType = FileUtilPlus.Files.advTypeOf(bufferedInputStream);
        System.out.println("文件类型(FileType)为：" + fileType);
        Assertions.assertEquals(FileType.Jpeg, fileType);

        Metadata metadata = ImageMetadataReader.readMetadata(bufferedInputStream);
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        GeoLocation location = gpsDirectory.getGeoLocation();
        System.out.println("时间：" + gpsDirectory.getGpsDate());
        System.out.println("经度：" + location.getLongitude());
        System.out.println("纬度：" + location.getLatitude());
    }

}