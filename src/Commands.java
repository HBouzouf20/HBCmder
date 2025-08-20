import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Scanner;

/**
 * cd, ls, mkdir, pwd, rm, mv, cp, touch , echo
 */
public class Commands {
    private Path currentPath = Constants.CURRENT_PATH;
    private Scanner scanner;
    public Commands() throws IOException {
        scanner = new Scanner(System.in);

        if (!new File(currentPath.toString()).exists()) {
            throw new FileSystemNotFoundException();
        } else {
            System.out.printf("""
                    HBCmder [Version 1.0.0.0]
                    (c) HBDeveloper Corporation. All rights reserved.
                    %s>""", currentPath.toString());
        }
        starter();
    }
    public void starter() throws IOException {
        String commandLine ;
        while (!(commandLine = scanner.nextLine()).equals("exit")) {
            if (commandLine.equals("ls")){
                ls();
            }
            else if (commandLine.contains("cd ") || commandLine.startsWith("cd")) {
                String path = commandLine.replace("cd ", "");
                path = path.replace(" ", "");
                cd(path); //cd path
            }else if (commandLine.equals("pwd")){
                pwd();
            }
            else if(commandLine.isEmpty()){

            }
            else {
                System.err.println("Unknown command");
            }
            System.out.print(currentPath+">");
        }
    }
    public void ls() throws IOException {
        File file = new File(currentPath.toString());
        StringBuilder result = new StringBuilder(String.format("""
                %-30s%-60s%-30s%-30s
                
                """, "permissions", "Create dAt", "Length", "fileName"));
        for (File f : file.listFiles(new HiddenFileFilter())) {
            String permissions = String.format("%s%s%s%s",
                    f.isDirectory() ? 'd' : '-', f.canRead() ? 'r' : '-', f.canWrite() ? 'w' : '-', f.canExecute() ? 'x' : '-'
            );
            BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
            FileTime createdAt = attr.creationTime();
            long length = f.length();
            String fileName = f.getName();
            if (f.isDirectory()) {
                result.append("\u001B[33m");
            }
            else {
                result.append("\u001B[0m");
            }
            result.append(String.format("""
                    %-30s%-60s%-30s%-30s
                    """, permissions, createdAt, length, fileName));


        }
        System.out.println(result+"\u001B[0m");
    }
    //setter -> CURRENT_PATH
    public void cd(String path) throws IOException {
        File currentFile = new File(this.currentPath.toString());
        if (path.isEmpty()){
            System.err.println("\nMissing path parameter !");
        }
        if (path.equals("..")){
            currentFile = currentFile.getParentFile();
        }
        else {
            Path newPath = Path.of(String.valueOf(currentFile.toPath()), path);
            if (Files.exists(newPath)) {
                currentFile = new File(newPath.toString());
            }
            else {
                System.err.println("Invalid path parameter !");;
            }
        }
        this.currentPath = Path.of(currentFile.getPath());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void pwd() throws IOException {
        System.out.println("PATH \n \u001B[33m" +currentPath.toString() + "\u001B[0m");
    }
}
