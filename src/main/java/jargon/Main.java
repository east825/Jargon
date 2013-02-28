package jargon;

import java.util.List;

/**
 * User: Mikhail Golubev
 * Date: 14.02.13
 * Time: 2:06
 */
public class Main {
    public static void main(String[] args) {
        OptionParser parser = OptionParser.newInstance("Test program").help(
                "Just sample program. Assume it's some kind of archive program"
        ).build();
        Flag compressFlag = Options.newFlagOption("--compress", "-c").help("compress chosen file").build();
        parser.addOption(compressFlag);
        Flag decompressFlag = Options.newFlagOption("--decompress", "-d").help("decompress chose file").build();
        parser.addOption(decompressFlag);
        Option<String> fileNameOption = Options.newStringOption("--file-name", "-f").help("target file").required().build();
        parser.addOption(fileNameOption);
        Option<Integer> compressionLevel = Options.newIntegerOption("-l", "--level").help("Compression level").defaultValue(0).build();
        parser.addOption(compressionLevel);
        MultiOption<String> filesToArchive = Options.newStringOption("-F", "--outputfiles").help("Files to archive").nargs("*").build();
        parser.addOption(filesToArchive);

        List<String> pos = parser.parse(args);
        System.out.println(pos);
        System.out.println("Compress: " + compressFlag.isSet());
        System.out.println("Decompress: " + decompressFlag.isSet());
        System.out.println("File name: " + fileNameOption.getValue());
        System.out.println("Compression level: " + compressionLevel.getValue());
        System.out.println("Files to archive: " + filesToArchive.getValue());

    }
}
