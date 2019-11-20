package tingtel.app.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class NetworksCode {


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="code")
    private String code;
    @ColumnInfo(name="desc")
    private String desc;
    @ColumnInfo(name="tag")
    private String tag;

    public NetworksCode(String name, String title, String code, String desc, String tag) {

        this.name = name;
        this.title = title;
        this.code = code;
        this.desc = desc;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public static NetworksCode[] populateNetworksCodes() {

        return new NetworksCode[] {

                new NetworksCode("airtel", " Check Airtel Balance", "*123#", "This service enables you to check your current airtime balance which is available for calls", "tag"),
                new NetworksCode("airtel", " Check Data Balance", "*141*11*0#", "This service allows you to check your current data balance(in MB) which is available for internet service", "tag"),
                new NetworksCode("airtel", " Borrow Airtime", "*500*amt#", "This service allows you to borrow airtime from Airtel./namt: the amount you want to borrow", "tag"),
                new NetworksCode("airtel", " Please call back", "*140*num#", "This service enables you to place a call back message to any of your contacts", "tag"),
                new NetworksCode("airtel", " Call customer care", "111", "This number helps you to call Airtel customer service", "tag"),
                new NetworksCode("airtel", " Check Phone Number", "*121*3*4#", "This service enables you to know your Airtel phone number", "tag"),
                new NetworksCode("airtel", " Transfer Airtime", "*901#", "This Airtel service enables you to make airtime transfer to another number. you have to enter the amount to be transfer and the recipient number in the fields provided.After clicking send, this will automatically send an SMS to 432 which will trigger the transaction", "tag"),

                new NetworksCode("airtel", " N300 data plan: 250MB/25days", "*885*1#", "With this plan, Airtel gives you 250MB of data for just N300 and it’s valid for 25 days (3 weeks)", "tag"),
                new NetworksCode("airtel", " N100 data plan(5MB daily): 25MB/5days", "*401#", "With this plan, Airtel gives you 25MB of data for just N100 and it’s valid for 5 days (5MB daily)", "tag"),
                new NetworksCode("airtel", " Airtel 6X airtime bonus", "*500*pin#", "With this service, you can benefit from Airtel 6X airtime bonus which will give you extra airtime for calls", "tag"),
                new NetworksCode("airtel", " Airtel 8X airtime bonus", "*126*pin#", "With this service, you can benefit from Airtel 8X airtime bonus which will give you extra airtime for calls", "tag"),
               new NetworksCode("airtel", " Change your Me2U code", "PIN 123++", "This service enables you to change your Me2U code. You have to enter the old PIN, then the new PIN in the fields provided. This will send an SMS containing your new PIN to 432", "tag"),
                new NetworksCode("airtel", " N1000 data plan: 1.5GB/30days", "*496#", "With this plan, Airtel gives you 1.5GB of data for just N1000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N1000 Ultra sms pack: 500sms/15days", "*160*500#", "With this plan, Airtel gives you 500sms for N1000 and it’s valid for 15 days (2 weeks)", "tag"),
                new NetworksCode("airtel", " N100 data plan: 50MB/24hrs", "*410#", "This is the major codeWith this plan, Airtel gives you 50MB of data for just N100 and it’s valid for 24 hours","tag"),
                new NetworksCode("airtel", " N136000 MEGA136 data plan: 200GB/365days", "*408#", "With this plan, Airtel gives you 200GB of data for just N136000 and it’s valid for 365 days (1 year)", "tag"),
                new NetworksCode("airtel", " N1500 data plan: 3GB/30days", "*435#", "With this plan, Airtel gives you 3GB of data for just N1500 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N1500 Platinum sms pack: 1000sms/30days", "*160*1000#", "With this plan, Airtel gives you 1000sms for N1500 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N2000 data plan: 3.5GB/30days", "*437#", "With this plan, Airtel gives you 3.5GB of data for just N2000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N200 data plan: 200MB/3days", "*412#", "With this plan, Airtel gives you 200MB of data for just N200 and it’s valid for 3 days", "tag"),
                new NetworksCode("airtel", " N200 Silver sms pack: 100sms/7days", "*160*100#", "With this plan, Airtel gives you 100sms for N200 and it’s valid for 7 days (1 week)", "tag"),
                new NetworksCode("airtel", " N2500 data plan: 5GB/30days", "*437*1#", "With this plan, Airtel gives you 5GB of data for just N2500 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N25 Lite sms pack: 10sms/3days", "*160*10#", "With this plan, Airtel gives you 10sms for N25 and it’s valid for 3 days (72 hours)", "tag"),
                new NetworksCode("airtel", " N3000 Diamond sms pack: 2500sms/30days", "*160*2500#", "With this plan, Airtel gives you 2500sms for N3000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N300 data plan: 350MB/7days", "*417#", "With this plan, Airtel gives you 350MB of data for just N300 and it’s valid for 7 days (1 week)", "tag"),
                new NetworksCode("airtel", " N3500 data plan: 7GB/30days", "*438#", "With this plan, Airtel gives you 7GB of data for just N3500 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N36000 MEGA36 data plan: 50GB/180days", "*406#", "With this plan, Airtel gives you 50GB of data for just N36000 and it’s valid for 180 days (6 months) ", "tag"),
                new NetworksCode("airtel", " N4000 data plan: 9GB/30days", "*438*1#", "With this plan, Airtel gives you 9GB of data for just N4000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N5000 MEGA5 data plan: 10GB/30days", "*452#", "With this plan, Airtel gives you 10GB of data for just N5000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N500 data plan: 750MB/14days", "*418#", "With this plan, Airtel gives you 750MB of data for just N500 and it’s valid for 14 days (2 weeks)", "tag"),
                new NetworksCode("airtel", " N50 Max sms pack: 20sms/5days", "*160*20#", "With this plan, Airtel gives you 20sms for N50 and it’s valid for 5 days (120 hours)", "tag"),
                new NetworksCode("airtel", " N525 data plan: 200MB/7days", "*440*17#", "With this plan, Airtel gives you 200MB of data for just N525 and it’s valid for 7 days (1 week)", "tag"),
                new NetworksCode("airtel", " N70000 MEGA70 data plan: 100GB/365days", "*407#", "With this plan, Airtel gives you 100GB of data for just N70000 and it’s valid for 365 days (1 year)", "tag"),
                new NetworksCode("airtel", " N8000 MEGA8 data plan: 16GB/30days", "*460#", "With this plan, Airtel gives you 16GB of data for just N8000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " Please credit me", "*141*8*num#", "This Airtel service allows you to ask someone to credit your phone for you", "tag"),
                new NetworksCode("airtel", " N10000 data plan: Unlimited/30days", "*462*10#", "With this plan, Airtel gives you Unlimited data for just N10000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N15000 data plan: Unlimited/30days", "*462*15#", "With this plan, Airtel gives you Unlimited data for just N15000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("airtel", " N20000 data plan: Unlimited/30days", "*462*20#", "With this plan, Airtel gives you Unlimited data for just N20000 and it’s valid for 30 days (1 month)", "tag"),


                new NetworksCode("9mobile", " Check Airtime Balance", "*232#", "This service code enables you to check your current airtime balance available for calls towards all network operators", "tag"),
                new NetworksCode("9mobile", " Check Data Balance", "*228#", "This service allows you to check your current data balance(in MB) available for internet service", "tag"),
                new NetworksCode("9mobile", " Check Phone Number", "*248#", "This service allows you to check your Phone Number", "tag"),
                new NetworksCode("9mobile", " Borrow Airtime", "*665#", "With this service, you can borrow airtime from 9mobile and pay back later./n amt: the amount you want to borrow", "tag"),
                new NetworksCode("9mobile", " Call customer care", "200", "This number enables you to call 9mobile customer service", "tag"),

                new NetworksCode("9mobile", " N200: 1GB night plan(12am-5am)/1day", "*229*3*11#", "With this plan, 9mobile gives you 1GB of data for N200 and it’s valid for 1 day (12am-5am)", "tag"),
                new NetworksCode("9mobile", " Activate 250% real bonus", "*611*1#", "This service gives you 250% real bonus for calls", "tag"),
               new NetworksCode("9mobile", " Check eligibility to borrow credit", "*665*3#", "This service enables you to know your eligibility to borrow airtime from 9mobile", "tag"),
               new NetworksCode("9mobile", " N10000 data plan: 15GB/30days", "*229*4*1#", "With this plan, 9mobile gives you 15GB of data for N10000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N1000 data plan: 1GB/30days", "*229*2*7#", "With this plan, 9mobile gives you 1GB of data for N1000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N1000 evening/weekend plan(7pm-7am): 2GB/30days", "*229*3*12#", "With this plan, 9mobile gives you 2GB of data for N1000 and it’s valid for 7 days (7pm-7am)", "tag"),
                new NetworksCode("9mobile", " N100 data plan: 40MB/24hrs", "*229*3*1#", "With this plan, 9mobile gives you 40MB of data for N100 and it’s valid for 24 hours (1 day)", "tag"),
                new NetworksCode("9mobile", " N100 facebook,twitter,instagram,eskimi,whatsApp,BBM,wechat/1day", "*343*6*7#", "With this plan, 9mobile gives you facebook,twitter,instagram,eskimi,whatsApp,BBM,wechat data for N100 and it’s valid for 1 day (24 hours)", "tag"),
                new NetworksCode("9mobile", " N11000 data plan: 120GB/365days", "*229*5*3#", "With this plan, 9mobile gives you 120GB of data for N110000 and it’s valid for 365 days (1 year)", "tag"),
                new NetworksCode("9mobile", " N1200 data plan: 1.5GB/30days", "*229*2*25#", "With this plan, 9mobile gives you 1.5GB of data for N1200 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N200 data plan: 150MB/7days", "*229*2*10#", "With this plan, 9mobile gives you 150MB of data for N200 and it’s valid for 7 days (1 week)", "tag"),
                new NetworksCode("9mobile", " N150 WhatsApp, BBM and wechat/1week", "*343*5*6#", "With this plan, 9mobile gives you whatsApp, BBM and wechat data for N150 and it’s valid for 7 days (1 week)", "tag"),
                new NetworksCode("9mobile", " N18000 data plan: 27.5GB/30days", "*229*4*3#", "With this plan, 9mobile gives you 27.5GB of data for N18000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N2000 data plan: 2.5GB/30day", "*229*2*8#", "With this plan, 9mobile gives you 2.5GB of data for N2000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N2000 evening/weekend plan(7pm-7am): 5GB/30days", "*229*3*13#", "With this plan, 9mobile gives you 5GB of data for N2000 and it’s valid for 30 days (7pm-7am)", "tag"),
                new NetworksCode("9mobile", " N27500 data plan: 30GB/90days", "*229*5*1#", "With this plan, 9mobile gives you 30GB of data for N27500 and it’s valid for 90 days (3 months)", "tag"),
                new NetworksCode("9mobile", " N3000 data plan: 4GB/30days", "*229*2*35#", "With this plan, 9mobile gives you 4GB of data for N3000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N300 facebook,twitter,instagram,eskimi,whatsApp,BBM,wechat/1week", "*343*6*8#", "With this plan, 9mobile gives you facebook,twitter,instagram,eskimi,whatsApp,BBM,wechat data for N300 and it’s valid for 7 days (1 week)", "tag"),
                new NetworksCode("9mobile", " N4000 data plan: 5.5GB/30days", "*229*2*36#", "With this plan, 9mobile gives you 5.5GB of data for N4000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N400 WhatsApp, BBM and wechat/1Month", "*343*5*7#", "With this plan, 9mobile gives you whatsApp, BBM and wechat data for N400 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N500 data plan: 500MB/30days", "*229*2*12#", "With this plan, 9mobile gives you 500MB of data for N500 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N50 data plan: 10MB/24hrs", "*229*3*8#", "With this plan, 9mobile gives you 10MB of data for N50 and it’s valid for 24 hours (1 day)", "tag"),
                new NetworksCode("9mobile", " N50 WhatsApp, BBM and wechat/1day", "*343*5*5#", "With this plan, 9mobile gives you whatsApp, BBM and wechat data for N50 and it’s valid for 1 day (24 hours)", "tag"),
                new NetworksCode("9mobile", " N55000 data plan: 60GB/120days", "*229*5*2#", "With this plan, 9mobile gives you 60GB of data for N55000 and it’s valid for 120 days (4 months)", "tag"),
                new NetworksCode("9mobile", " N700 facebook,twitter,instagram,eskimi,whatsApp,BBM,wechat/1week", "*343*6*9#", "With this plan, 9mobile gives you facebook,twitter,instagram,eskimi,whatsApp,BBM,wechat data for N700 and it’s valid for 7 days (1 week)", "tag"),
                new NetworksCode("9mobile", " N8000 data plan: 11.5GB/30days", "*229*2*5#", "With this plan, 9mobile gives you 11.5GB of data for N8000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " N84992 data plan: 100GB/30days", "*229*4*5#", "With this plan, 9mobile gives you 100GB of data for N84992 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("9mobile", " Register a you and me number", "*233*1*num#", "Etisalat You and Me is a reward scheme which allows easy starter customers enjoy free credit every week to call an Etisalat You and Me special number anytime.If you opt-in for Etisalat U and Me, you will get N100 free credit during the week you recharge N100 and if you recharge up to N200, you will get N300 free credit. You can enjoy up to N1200 free credit every month by recharging with a minimum of N200 per week", "tag"),
                new NetworksCode("9mobile", " N400 2 hours of uninterrupted videos from any app", "*229*3*5#", "With this plan, 9mobile gives you 2hours of Uninterrupted videos from any app just for N400./nvalidity: 2hours", "tag"),
                new NetworksCode("9mobile", " Join etisalat you and me", "*244*2#", "Etisalat You and Me is a reward scheme which allows easy starter customers enjoy free credit every week to call an Etisalat You and Me special number anytime.If you opt-in for Etisalat U and Me, you will get N100 free credit during the week you recharge N100 and if you recharge up to N200, you will get N300 free credit. You can enjoy up to N1200 free credit every month by recharging with a minimum of N200 per week", "tag"),


                new NetworksCode("glo", " Check Airtime Balance", "*124*1#", "This service code will give you your account balance", "tag"),
                new NetworksCode("glo", " Check Data Balance", "*127*0#", "This service code helps you to check your data balance(in MB) which is available for internet service", "tag"),

                new NetworksCode("glo", " Check my phone number", "*135*8#", "Are you always having difficulties remembering your Glo phone number? then this code will help you!", "tag"),
                new NetworksCode("glo", "Buy data plans", "*777#", "Dialing this code will give you the list of all Glo data plans available on the network which you can then chose from", "tag"),
                new NetworksCode("glo", " Borrow Airtime", "*321#", "This service enables you to borrow airtime for calls towards all networks from Glo and pay back later", "tag"),
                new NetworksCode("glo", " Call customer care", "121", "This service allows you to call Glo customer service", "tag"),
                new NetworksCode("glo", " Check credit", "#124#", "This service will enable you to get your current airtime balance available for calls towards all network operators", "tag"),
               new NetworksCode("glo", " N10000 data plan; 26GB/30day", "*127*11#", "With this Glo data plan, you get a whole 26GB of data plus a bonus of 6.5GB summing up to 32.5GB of data for just N10,000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N1000 data plan; 1.6GB/30day", "*127*53#", "With this Glo data plan, you get 1.6GB of data plus a bonus of 400MB summing up to 2GB of data for just N1000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N100 data plan; 80+20MB/1day", "*127*51#", "With this Glo data plan, you get 80MB + 20MB of data for just N100 and it’s valid for 24 hours (1 day)", "tag"),
                new NetworksCode("glo", " N15000 data plan; 42GB/30day", "*127*12#", "With this Glo data plan, you get a whole 42GB of data plus a bonus of 10.5GB summing up to 52.5GB of data for N15,000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N18000 data plan; 50GB/30day", "*127*13#", "With this Glo data plan, you get a whole 50GB of data plus a bonus of 12.5GB summing up to 62.5GB of data for just N18,000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N2000 data plan; 3.65GB/30day(My Phone Plan)", "*127*55#", "With this Glo data plan, you get 3.65GB of data plus a bonus of 900MB summing up to 4.5GB for just N2000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N200 data plan; 200MB/5day", "*127*56#", "With this Glo data plan, you get 210MB of data plus a bonus of 52MB summing up to 262MB of data for just N200 and it’s valid for 5 days", "tag"),
                new NetworksCode("glo", " N2500 data plan; 5.75GB/30day(So Special)", "*127*58#", "With this Glo data plan, you get a whole 5.75GB of data plus a bonus of 1.45GB summing up to 7.2GB of data for just N2,500 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N3000 data plan; 7GB/30day(Always Macro)", "*127*54#", "With this Glo data pln, you get a whole 7GB of data plus a bonus of 1.75GB summing up to 8.75GB of data for just N3,000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N4000 data plan; 10GB/30day", "*127*59#", "With this Glo data plan, you get a whole 10GB of data plus a bonus of 2.5GB summing up to 12.5GB of data for just N4,000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N5000 data plan; 12.5GB/30day(Always Min)", "*127*2#", "With this Glo data plan, you get a whole 12.5GB of data plus a bonus of 3.1GB summing up to 15.6GB of data for just N5,000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " N500 data plan; 800MB/14day", "*127*57#", "With this Glo data plan, you get 800MB of data plus 200MB summing up to 1GB of data for just N500 and it’s valid for 14 days (2 weeks)", "tag"),
                new NetworksCode("glo", " N50 data plan; 30MB/1day", "*127*14#", "With this Glo data plan, you get 30MB of data for just N50 and it’s valid for 24 hours", "tag"),
                new NetworksCode("glo", " N8000 data plan; 20GB/30day(Always Max)", "*127*1#", "With this Glo data plan, you get a whole 20GB of data plus a bonus of 5GB summing up 25GB of data for just N8,000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " Glo facebook", "*206#", "This service gives you a data bundle that can be used can be used only for facebook", "tag"),
                new NetworksCode("glo", " G100 N6000; 4GB/30days", "*127*5#", "With this Glo data plan, you get a whole 4GB of data for just N6,000 and it’s valid for 30 days (1 month)", "tag"),
                new NetworksCode("glo", " G300 N15000; 12GB/3Months", "*127*4#", "With this Glo data plan, you get a whole 12GB of data for just N15,000 and it’s valid for 90 days (3 month)", "tag"),
                new NetworksCode("glo", " G_Leisure N5000; 12GB/8PM-9AM weekdays", "*127*7#", "This Glo data plan offers you 12GB for just N5000 valid from 8PM to 9AM on weekdays", "tag"),
                new NetworksCode("glo", " G_Work N6000; 12GB/8PM-9AM", "*127*6#", "With G_Work Glo data plan, you get 12GB for just N6000 valid from 8PM to 9AM on weekdays", "tag"),
                new NetworksCode("glo", " N200 night plan; 1GB/1day", "*127*60#", "With this, Glo gives you 1GB which is usable only at night and is valid for 1day", "tag"),
                new NetworksCode("glo", " N500_weekend_plan; 3GB/7days", "*127*61#", "With this, Glo gives you 3GB which is usable only on weekends and is valid for 7days", "tag"),
                new NetworksCode("glo", " Please call back", "*125*num#", "This Glo service enables you send a call back to any Glo subscriber. The most special thing about 9jaCodes is that you don't have to remember the recipient number, 9jaCodes will pick this contact from your contacts list for you", "tag"),
                new NetworksCode("glo", " Share Data", "*127*01*num#", "With this service, you can share your data bundle with any other subscriber. just click the button and fill the required fields, amazingly 9jaCodes will pick the recipient phone number from your contacts list for you", "tag"),

                new NetworksCode("mtn", " Check Airtime", "*556#", "This service will enable you to get your current airtime balance available for calls towards MTN subscribers and all other network operators", "ussd"),
                new NetworksCode("mtn", " Check Airtime 2", "*123*1*3#", "This ussd code allows you check your MTN airtime balance for calls", "ussd"),
                new NetworksCode("mtn", " Check Data balance", "*131*4#", "This service allows you check your data balance on mtn network. This can be used for internet services", "tag"),
                new NetworksCode("mtn", " Check Data balance 2", "*559#", "This service code helps you to check your data balance(in MB) which is available for internet service", "tag"),
                new NetworksCode("mtn", " Recharge Airtime", "*555*PIN#", "This service code allows you recharge your mtn sim", "recharge"),
                new NetworksCode("mtn", " Borrow Airtime", "*606#", "This service code allows your borrow either airtime or data to be paid later", "ussd"),
                new NetworksCode("mtn", " Buy Data", "*131#", "This service code allows you to subscribe to any mtn data plan of your choice", "ussd"),
                new NetworksCode("mtn", " Call customer care", "180", "Dial this number to call MTN customer service", "call"),
                new NetworksCode("mtn", " Please Call Me Back", "*133*1*num#", "This service code allows you request other users to call you back", "ussd"),
                new NetworksCode("mtn", " Please Send Me Credit", "*133*2*num#", "This service code allows you request other users to send you airtime", "ussd"),
                new NetworksCode("mtn", " Cancel Data Auto-Renewal", "*123*5#", "This service code allows you unsubscribe from all mtn data plans auto renewal", "ussd"),
                new NetworksCode("mtn", " Change My Tariff Plan", "*123*1*2#", "This service code allows you unsubscribe from all mtn data plans auto renewal", "ussd"),
                new NetworksCode("mtn", " Change My Tariff Plan", "*123*1*2#", "This service code allows you to change your mtn tariff plan", "ussd"),
                new NetworksCode("mtn", " Change transfer pin", "*600*oldpin*newpin*newpin#", "This service helps you to change your airtime transfer Personal Identification Number (PIN). After entering the code, you enter the old PIN and enter the new PIN twice", "tag"),
                new NetworksCode("mtn", " Check account balance (summary)", "*141#", "This service will give you the summary of your account balance", "tag"),
                new NetworksCode("mtn", " Check detail account balance", "*141*1#", "This will give you your account balance and every details associated with the balance", "tag"),
                new NetworksCode("mtn", " To know my phone number", "*123#", "This code will help you to know your MTN phone number if at any time you can't remember it", "tag"),
                new NetworksCode("mtn", " N10000: 22GB/30days", "*117#", "With this plan, MTN gives you 22GB of data for N10,000 and it’s valid for 30 days (one month)", "tag"),
                new NetworksCode("mtn", " N1000: 1.5GB/30days", "*106#", "With this plan, MTN gives you 1GB (+500mb bonus data) of data for just N1000 and it’s valid for 30 days (one month)", "tag"),
                new NetworksCode("mtn", " N100: 50MB/1day", "*104#", "With this plan, MTN gives you 50MB of data for just N100 and it’s valid for 24 hours", "tag"),
                new NetworksCode("mtn", " N20000: 50GB/60days", "*118#", "With this plan, MTN gives you 50GB of data for N20,000 and it’s valid for 60 days (two month) Activation code: Dial *118# or Text 118 to 131 on your MTN line to subscribe", "tag"),
                new NetworksCode("mtn", " N2000: 3.5GB/30days", "*110#", "With this plan, MTN gives you 3.5GB (+500mb bonus data) of data for just N2000 and it’s valid for 30 days (one month)", "tag"),
                new NetworksCode("mtn", " N200: 150MB/1day", "*113#", "With this plan, MTN gives you 150MB of data for just N200 and it’s valid for 24 hours", "tag"),
                new NetworksCode("mtn", " N300: 150MB/7days", "*102#", "With this plan, MTN gives you 150MB of data for just N300 and it’s valid for 7 days (one week) Activation code: Dial *102# or Text 102 to 131 on your MTN line to subscribe", "tag"),
                new NetworksCode("mtn", " N3500: 5GB/30days", "*107#", "With this plan, MTN gives you 5GB of data for just N3,500 and it’s valid for 30 days (one month)", "tag"),
                new NetworksCode("mtn", " N50000: 85GB/90days", "*133#", "With this plan, MTN gives you 85GB of data for N50,000 and it’s valid for 90 days (three month) Activation code: Dial *133# or Text 133 to 131 on your MTN line to subscribe", "tag"),
                new NetworksCode("mtn", " N5000: 10GB/30days", "*116#", "With this plan, MTN gives you 10GB of data for N5000 and it’s valid for 30 days (one month)", "tag"),
                new NetworksCode("mtn", " N500: 750MB/7days", "*103#", "With this plan, MTN gives you 750MB of data for just N500 and it’s valid for 7 days (one week) Activation code: Dial *103# or Text 103 to 131 on your MTN line to subscribe", "tag"),
                new NetworksCode("mtn", " Please call back", "*133*num#", "This service enables you to send a call back to any contact. Amazingly 9jaCodes will help you to pick the recipient number from your contacts list if you can't remember it", "tag"),
                new NetworksCode("mtn", " Please send me credit", "*133*2*num#", "With this MTN service, you can ask someone to credit your phone for you", "tag"),
                new NetworksCode("mtn", " Sms bonuses", "*159*3#", "This service gives you access to all SMS bonuses available on the network", "tag"),
                new NetworksCode("mtn", " Transfer airtime", "*600*num*amt*pin#", "This service helps you to transfer MTN airtime to another subscriber. The amazing thing about 9jaCodes is that you don't have to remember the recipient number before making this transaction, you can just pick this number from your contacts list", "tag"),



        };
    }
}

