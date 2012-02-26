package com.jtaylor.util;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegPhotoshopMetadata;
import org.apache.sanselan.formats.jpeg.iptc.IPTCRecord;
import org.apache.sanselan.formats.jpeg.iptc.IPTCType;
import org.apache.sanselan.formats.jpeg.iptc.JpegIptcRewriter;
import org.apache.sanselan.formats.jpeg.iptc.PhotoshopApp13Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jtaylor
 * Date: 1/28/11
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class XMPOperations
{
   public static void writeKeywordsToFile(File file,List<String> keywords) throws IOException, ImageReadException, ImageWriteException
   {
      IImageMetadata metadata = Sanselan.getMetadata(file);
      if(metadata instanceof JpegImageMetadata)
      {
         JpegImageMetadata jpegMetadata=(JpegImageMetadata)metadata;
         JpegPhotoshopMetadata jpegPhotoshopMetadata=((JpegImageMetadata) metadata).getPhotoshop();
         PhotoshopApp13Data photoshop13Metadata=jpegPhotoshopMetadata.photoshopApp13Data;
         List<IPTCRecord> records=photoshop13Metadata.getRecords();
         IPTCType keywordsType=new IPTCType(25,"Keywords");
         for(String keyword:keywords)
         {
            records.add(new IPTCRecord(keywordsType,keyword));
         }
         PhotoshopApp13Data newOutputData=new PhotoshopApp13Data(records,photoshop13Metadata.getRawBlocks());
         File tempFile=FileOperations.getUniqueFile(FileOperations.getTempFolder(),file.getName());
         new JpegIptcRewriter().writeIPTC(file, new FileOutputStream(tempFile),newOutputData);
         if(!tempFile.renameTo(file))
         {
//            log.debug("could not overwrite, deleting");
            file.delete();
            tempFile.renameTo(file);
         }
      }
      else
      {
         throw new AbstractMethodError("unimplemented for tiff files");
      }
   }
   public static void main(String[] args)
   {
      try
      {
         File file=new File("/Users/jtaylor/Desktop/xmpTest.jpg");
         File outFile=new File("/Users/jtaylor/Desktop/xmpTestOut.jpg");
         IImageMetadata metadata = Sanselan.getMetadata(file);

         //System.out.println(metadata);

         if (metadata instanceof JpegImageMetadata)
         {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            // Jpeg EXIF metadata is stored in a TIFF-based directory structure
            // and is identified with TIFF tags.
            // Here we look for the "x resolution" tag, but
            // we could just as easily search for any other tag.
            //
            // see the TiffConstants file for a list of TIFF tags.

            System.out.println("file: " + file.getPath());
            System.out.println("photoshop");
            Iterator<ImageMetadata.Item> iterator=jpegMetadata.getPhotoshop().getItems().iterator();
            ImageMetadata.Item item;
            while(iterator.hasNext())
            {
               item=iterator.next();
               System.out.println(item.getKeyword()+" : "+item.getText());
            }
            JpegPhotoshopMetadata outputMetadata=jpegMetadata.getPhotoshop();
            PhotoshopApp13Data outputData=outputMetadata.photoshopApp13Data;
//            outputMetadata.add("Keywords","testKeywordJava1");
//            System.out.println("added new keyword: testKeywordJava1");
//            IPTCRecord record;
//            System.out.println("photoshop metadata");
//            for(Object o:outputMetadata.getItems())
//            {
//               item=(ImageMetadata.Item)o;
//               System.out.println(item.getKeyword()+" : "+item.getText());
//            }
            List<IPTCRecord> records=outputData.getRecords();
            records.add(new IPTCRecord(new IPTCType(25,"Keywords"),"testKeywordJava2"));
            PhotoshopApp13Data newOutputData=new PhotoshopApp13Data(records,outputData.getRawBlocks());
            new JpegIptcRewriter().writeIPTC(file, new FileOutputStream(outFile),newOutputData);
            System.out.println("done");
//            IPTCRecord record;
//            System.out.println("photoshop 13 data");
//            for(Object o:outputData.getRecords())
//            {
//               record=(IPTCRecord)o;
//               System.out.println(record.getIptcTypeName()+" : "+record.getValue()+" : "+record.iptcType.type+" : "+record.iptcType.name);
//            }
            // print out various interesting EXIF tags.
//            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_XPKEYWORDS);
//            printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_XRESOLUTION);
//            printTagValue(jpegMetadata, TiffConstants.TIFF_TAG_DATE_TIME);
//            printTagValue(jpegMetadata,
//                  TiffConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
//            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_CREATE_DATE);
//            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_ISO);
//            printTagValue(jpegMetadata,
//                  TiffConstants.EXIF_TAG_SHUTTER_SPEED_VALUE);
//            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_APERTURE_VALUE);
//            printTagValue(jpegMetadata, TiffConstants.EXIF_TAG_BRIGHTNESS_VALUE);
//            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
//            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LATITUDE);
//            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
//            printTagValue(jpegMetadata, TiffConstants.GPS_TAG_GPS_LONGITUDE);
         }
      }
      catch(Exception e)
      {
         System.err.println("there was an error running the test");
         e.printStackTrace();
      }
   }
//   private static void printTagValue(JpegImageMetadata jpegMetadata,
//                                     TagInfo tagInfo)
//   {
//      jpegMetadata.
//      TiffField field = jpegMetadata.findEXIFValue(tagInfo);
//      if (field == null)
//         System.out.println(tagInfo.name + ": " + "Not Found.");
//      else
//         System.out.println(tagInfo.name + ": "
//               + field.getValueDescription());
//   }
}
