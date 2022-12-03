package nl.utwente.proverb.analyzer.pvbanalyzer.mdparser;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.analyzer.pvbanalyzer.config.PVBConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
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
        mdTool.setName(name.replace(" ",""));
        try (var reader = new InputStreamReader(new FileInputStream(input));
             var br = new BufferedReader(reader)){
            ToolParser.Frag frag = readTilNextFrag(br);
            if (!frag.content.isEmpty()){
                //have some introduction before head
                mdTool.addProperty(frag.content.get(0), frag.content.subList(1, frag.getContent().size()));
            }
            while (frag.nextTitle != null) {
                switch (frag.nextTitle){
                    case MDToolTemplate.FULL_NAME:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.FULL_NAME, frag.getContent());
                        break;
                    case MDToolTemplate.DOMAIN:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.DOMAIN, frag.getContent());
                        break;
                    case MDToolTemplate.TYPE:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.TYPE, frag.getContent());
                        break;
                    case MDToolTemplate.INPUT_THING:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.INPUT_THING, frag.getContent());
                        break;
                    case MDToolTemplate.INPUT_FORMAT:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.INPUT_FORMAT, frag.getContent());
                        break;
                    case MDToolTemplate.OUTPUT:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.OUTPUT, frag.getContent());
                        break;
                    case MDToolTemplate.INTERNAL:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.INTERNAL, frag.getContent());
                        break;
                    case MDToolTemplate.COMMENT:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.COMMENT, frag.getContent());
                        break;
                    case MDToolTemplate.URIS:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.URIS, frag.getContent());
                        break;
                    case MDToolTemplate.LAST_COMMIT_DATE:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.LAST_COMMIT_DATE, frag.getContent());
                        break;
                    case MDToolTemplate.LAST_PUB_DATE:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.LAST_PUB_DATE, frag.getContent());
                        break;
                    case MDToolTemplate.PAPERS:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.PAPERS, frag.getContent());
                        break;
                    case MDToolTemplate.RELA_TOOLS:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.RELA_TOOLS, frag.getContent());
                        break;
                    case MDToolTemplate.META:
                        frag = readTilNextFrag(br);
                        mdTool.addProperty(MDToolTemplate.META, frag.getContent());
                        break;
                    default:
                        //Sentences that are not follow the template
                        if (!StringUtils.isBlank(frag.getNextTitle())){
                            String title = frag.nextTitle;
                            frag = readTilNextFrag(br);
                            mdTool.addProperty(title, frag.getContent());
                            log.warn("Undefined title in MD file {}: " + frag.getNextTitle(), name);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            log.error("Reading file error:" +e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
        }
        return mdTool;
    }

    public Frag readTilNextFrag(BufferedReader br) throws IOException{
        String line = br.readLine();
        List<String> content = new ArrayList<>();
        while(line!=null && !line.startsWith(MDToolTemplate.PREFIX)){
            content.add(line);
            line = br.readLine();
        }
        if (!content.isEmpty() && content.get(content.size()-1).equals("")){
            return new Frag(content.subList(0, content.size()-1), line);
        }
        return new Frag(content, line);
    }

    public void write(MDTool mdTool){
        try (var pw = new PrintWriter(new FileWriter(tool))){
            StringBuilder builder = new StringBuilder();
            for (var title : mdTool.getTitles()){
                builder.append(combineSeg(title, mdTool.getProperty(title)));
            }
            pw.print(builder.toString().trim()+"\n");
        }catch (IOException e){
            log.error(e.getStackTrace());
        }
    }

    public String combineSeg(String title, List<String> contents){
        StringBuilder builder = new StringBuilder();
        builder.append(title).append("\n");
        contents.forEach(c -> builder.append(c).append("\n"));
        builder.append("\n");
        return builder.toString();
    }

    @Getter
    @AllArgsConstructor
    static class Frag{
        List<String> content;

        String nextTitle;
    }
}
