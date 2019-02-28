package com.syswin.pipeline.service.org.impl;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 115477 on 2019/2/20.
 */
@Data
public class OrgDataTemail {
    private String temail;

    private String pubKey;

    private String vCard;

    private OrgDataVCard card;

    private String tags;

    public OrgDataVCard getCard() {
        if (card == null) {
            try {
                card = fromVCard(vCard);
            } catch (Exception e) {
                card = null;
            }
        }
        return card;
    }

    public List<String> getOrgs() {
        OrgDataVCard card = this.getCard();
        if (card != null && card.getOrg() != null) {
            String[] orgArr  = card.getOrg().split("/");
            return Arrays.asList(orgArr);
        }

        return new ArrayList<>();
    }

    private static OrgDataVCard fromVCard(String vCard) {

        VCard c = Ezvcard.parse(vCard).first();
        String name = c.getStructuredName().getFamily();
        String title = c.getTitles().get(0).getValue();
        String org = c.getOrganizations().get(0).getValues().get(0);

        return new OrgDataVCard(name, title, org);
    }


    public static void main(String[] args) {

        String vCard = "BEGIN:VCARD\\r\\nVERSION:3.0\\r\\nTEL;TYPE=work:13911971682\\nPHOTO;VALUE=uri:http://scloud.toon.mobi/f/wsT404YaL92V+yegR8kSQB6WKHbd-nRQcquSFPgFRvUfI.jpg\\nN:吴江\\nX-MSGSEAL-SPELL:wujiang\\nEMAIL:wujiang@msgseal.com\\nORG:思源总部/互联集团/产品统筹部\\nTITLE:高级技术支持工程师\\nUID:0\\nEND:VCARD\\r\\n";
        vCard = vCard.replaceAll("\\\\r\\\\n|\\\\n", "\n");
        System.out.println(vCard);
        System.out.println(fromVCard(vCard));
    }
}
