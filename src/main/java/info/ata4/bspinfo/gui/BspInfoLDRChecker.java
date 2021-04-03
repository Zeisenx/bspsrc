package info.ata4.bspinfo.gui;

import info.ata4.bsplib.BspFile;
import info.ata4.bsplib.BspFileReader;
import info.ata4.bspsrc.modules.BspCompileParams;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BspInfoLDRChecker {

    public static void CheckFile(File currentFile) throws IOException {
        BspFile bspFile;
        BspFileReader bspReader;

        bspFile = new BspFile();
        bspFile.load(currentFile.toPath());

        bspReader = new BspFileReader(bspFile);

        BspCompileParams cparams = new BspCompileParams(bspReader);

        if (cparams.isVradRun()) {
            if (StringUtils.join(cparams.getVradParams(), ' ').contains("-ldr") &&
                !StringUtils.join(cparams.getVradParams(), ' ').contains("-both"))
                System.out.println("- " + currentFile.getName());
        }
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:/Files/LDRCheckList/mapcycle.txt");
        ArrayList<String> mapCycle = new ArrayList<String>();
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            mapCycle.add(scan.nextLine());
        }

        for (String bspName : mapCycle){
            File bspFile = new File("D:/Files/LDRCheckList/maps/" + bspName + ".bsp");
            if (!bspFile.exists()) {
                System.out.println(bspName + " not Found!");
                continue;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CheckFile(bspFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
