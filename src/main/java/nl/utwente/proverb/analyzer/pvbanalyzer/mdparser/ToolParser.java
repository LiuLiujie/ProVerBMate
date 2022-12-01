package nl.utwente.proverb.analyzer.pvbanalyzer.mdparser;



import nl.utwente.proverb.analyzer.pvbanalyzer.config.PVBConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToolParser extends BaseParser {

    private final File tool;

    private final PVBConfiguration config;

    public ToolParser(File tool, PVBConfiguration configuration){
        this.tool = tool;
        this.config = configuration;
        this.parse();
    }

    public MDTool parse(){
        File input = tool;
        MDTool mdTool = new MDTool();
        var name = input.getName().replace(".md","");
        mdTool.setName(name);
        try (var reader = new InputStreamReader(new FileInputStream(input));
             var br = new BufferedReader(reader)){
            String line;
            line = br.readLine();
            while (line != null) {
                switch (line){
                    case MDToolTemplate.FULL_NAME:
                        var fName = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.FULL_NAME, fName);
                        break;
                    case MDToolTemplate.DOMAIN:
                        var domains = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.DOMAIN, domains);
                        break;
                    case MDToolTemplate.TYPE:
                        var types = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.TYPE, types);
                        break;
                    case MDToolTemplate.INPUT_THING:
                        List<String> thing = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.INPUT_THING, thing);
                        break;
                    case MDToolTemplate.INPUT_FORMAT:
                        List<String> inputs = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.INPUT_FORMAT, inputs);
                        break;
                    case MDToolTemplate.OUTPUT:
                        List<String> outputs = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.OUTPUT, outputs);
                        break;
                    case MDToolTemplate.INTERNAL:
                        List<String> internals = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.INTERNAL, internals);
                        break;
                    case MDToolTemplate.COMMENT:
                        List<String> comments = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.COMMENT, comments);
                        break;
                    case MDToolTemplate.URIS:
                        List<String> uris = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.URIS, uris);
                        break;
                    case MDToolTemplate.LAST_COMMIT_DATE:
                        List<String> cmtDate = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.LAST_COMMIT_DATE, cmtDate);
                        break;
                    case MDToolTemplate.LAST_PUB_DATE:
                        List<String> pubDate = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.LAST_PUB_DATE, pubDate);
                        break;
                    case MDToolTemplate.PAPERS:
                        List<String> papers = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.PAPERS, papers);
                        break;
                    case MDToolTemplate.RELA_TOOLS:
                        List<String> relaTools = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.RELA_TOOLS, relaTools);
                        break;
                    case MDToolTemplate.META:
                        List<String> metas = readTilEmpty(br);
                        mdTool.addProperty(MDToolTemplate.META, metas);
                        break;
                    default:
                        //Sentences that are not follow the template
                        if (!line.isBlank()){
                            List<String> defaults = readTilEmpty(br);
                            mdTool.addProperty(line, defaults);
                            System.out.println("Undefined title in MD file: " + line);
                        }
                        break;
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            System.err.println("Reading file error:" +e.getMessage());
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
        return mdTool;
    }

    public List<String> readTilEmpty(BufferedReader br) throws IOException{
        String line = br.readLine();
        List<String> content = new ArrayList<>();
        while (line != null && !line.isEmpty()){
            content.add(line);
            line = br.readLine();
        }
        return content;
    }

    public void write(MDTool mdTool){
        try (var pw = new PrintWriter(new FileWriter(tool))){
            StringBuilder builder = new StringBuilder();
            for (var title : mdTool.getTitles()){
                builder.append(combineSeg(title, mdTool.getProperty(title)));
            }
            pw.print(builder.toString().trim()+"\n");
        }catch (IOException e){
            System.err.println(e.getStackTrace().toString());
        }
    }

    public String combineSeg(String title, List<String> contents){
        StringBuilder builder = new StringBuilder();
        builder.append(title).append("\n");
        contents.forEach(c -> builder.append(c).append("\n"));
        builder.append("\n");
        return builder.toString();
    }
}
