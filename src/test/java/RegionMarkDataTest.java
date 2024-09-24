import mapext.common.marks.RegionMarkData;
import org.junit.Assert;
import org.junit.Test;

public class RegionMarkDataTest {

    @Test
    public void test0() {
        var data = new RegionMarkData();
        data.setMarkColor(0,0, 0xFF000000);
        data.setMarkColor(1,2, 0xABABABAB);
        data.setMarkColor(4,6, 0x12315656);
        data.setMarkColor(12,12, 0x48648686);
        Assert.assertEquals(0xFF000000, (int)data.getMarkColor(0,0));
        Assert.assertEquals(0xABABABAB, (int)data.getMarkColor(1,2));
        Assert.assertEquals(0x12315656, (int)data.getMarkColor(4,6));
        Assert.assertEquals(0x48648686, (int)data.getMarkColor(12,12));
        Assert.assertNull(data.getMarkColor(13, 15));
    }
}
