package lan.dk.podcastserver.Business;

import lan.dk.podcastserver.business.UpdatePodcastBusiness;
import lan.dk.podcastserver.config.BeanConfigScan;
import lan.dk.podcastserver.config.PropertyConfig;
import lan.dk.podcastserver.context.Mock.MockRepository;
import lan.dk.podcastserver.context.Mock.MockService;
import lan.dk.podcastserver.context.MockWorkerContextConfiguration;
import lan.dk.podcastserver.entity.Cover;
import lan.dk.podcastserver.entity.Podcast;
import lan.dk.podcastserver.manager.worker.updater.Updater;
import lan.dk.podcastserver.utils.WorkerUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 26/12/2013.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BeanConfigScan.class, PropertyConfig.class, MockRepository.class})
public class SignatureTest {

    private final Logger logger = LoggerFactory.getLogger(SignatureTest.class);

    @Resource
    UpdatePodcastBusiness updatePodcastBusiness;
    @Resource WorkerUtils workerUtils;
    List<Podcast> podcastList = new ArrayList<Podcast>();

    @Before
    public void initTest() {
        podcastList.add(new Podcast("Cauet", "http://www.youtube.com/channel/UCe2YQ986DdKliHNvE8va4kQ", "", "Youtube", null, null, new Cover(), null, null));
        podcastList.add(new Podcast("Will & Co", "http://www.youtube.com/channel/UCzMawL8sevUd5nZ14x4wRpg", "", "Youtube", null, null, new Cover(), null, null));
        podcastList.add(new Podcast("Geek Inc HD", "http://www.geekinc.fr/rss/geek-inc-hd.xml", "", "RSS", new Timestamp(System.currentTimeMillis()), null, null, null, null));
        podcastList.add(new Podcast("PodMyDev", "http://pipes.yahoo.com/pipes/pipe.run?URL=http%3A%2F%2Fwww.podmydev.com%2F%3Ffeed%3Dpodcast&_id=1c04fdbe524a909e33308bc4fab9d5ae&_render=rss", "", "RSS", new Timestamp(System.currentTimeMillis()), null, null, null, null));
        podcastList.add(new Podcast("TWIG", "http://feeds.twit.tv/twig_video_hd", "", "RSS", new Timestamp(System.currentTimeMillis()), null, null, null, null));
        podcastList.add(new Podcast("Le Petit Journal", "http://www.canalplus.fr/c-divertissement/c-le-petit-journal/pid6515-l-emission.html",
                "", "CanalPlus", null, null, new Cover("http://media7.canal-plus.net/image/25/2/319252.jpg", 60, 60), null, null));
        podcastList.add(new Podcast("La Météo de Doria", "http://www.canalplus.fr/c-divertissement/c-le-grand-journal/pid4688-la-meteo-de-doria.html",
                "", "CanalPlus", null, null, new Cover("http://media7.canal-plus.net/image/64/4/321644.jpg", 60, 60), null, null));
        podcastList.add(new Podcast("L'instant Barré", "http://www.canalplus.fr/lib/front_tools/ajax/wwwplus_live_onglet.php?pid=3847&ztid=5810&nbPlusVideos1=1",
                "", "CanalPlus", null, null, new Cover("http://img15.hostingpics.net/pics/966069Capture20131020092150.png", 60, 60), null, null));
        podcastList.add(new Podcast("Le Tube", "http://www.canalplus.fr/c-divertissement/pid6427-c-le-tube.html",
                "", "CanalPlus", null, null, new Cover("http://img15.hostingpics.net/pics/966069Capture20131020092150.png", 60, 60), null, null));
        podcastList.add(new Podcast("Le Before", "http://www.canalplus.fr/c-divertissement/c-le-before-du-grand-journal/pid6429-l-emission.html",
                "", "CanalPlus", null, null, new Cover("http://img15.hostingpics.net/pics/966069Capture20131020092150.png", 60, 60), null, null));

    }

    @After
    public void afterTest() {
        podcastList.clear();
    }

    @Test
    public void updatePodcast() throws Exception {

        Updater updater;

        for (Podcast podcast : podcastList) {
            logger.info(podcast.getTitle());
            Assert.isTrue(StringUtils.isEmpty(podcast.getSignature()));

            updater = workerUtils.getUpdaterByType(podcast);

            String signature = updater.signaturePodcast(podcast);
            if ( signature != null) {
                podcast.setSignature(signature);
            }
            logger.info("s1 : {}", podcast.getSignature());

            signature = updater.signaturePodcast(podcast);
            logger.info("s2 : {}", signature);

            Assert.isTrue(signature != null && signature.equals(podcast.getSignature()));

        }

    }
}