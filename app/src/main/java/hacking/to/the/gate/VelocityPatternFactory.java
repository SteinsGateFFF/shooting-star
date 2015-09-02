package hacking.to.the.gate;

/**
 * Created by yihuaqi on 2015/9/1.
 */
public class VelocityPatternFactory {

    public static VelocityPattern produce(int pattern, final Velocity initVelocity){
        switch (pattern){
            case VelocityPattern.WORM:

                return new VelocityPattern() {
                    Velocity mInitVelocity = initVelocity;
                    int count = 0;
                    @Override
                    public Velocity nextVelocity(Velocity v) {
                        count++;
                        double percentage = Math.pow((count%60)-30,2)/900d;
//                        double percentage = Math.abs((count%60)-30)/30d;
                        return mInitVelocity.change(percentage);

                    }
                };
            default: return null;
        }
    }
}
