package NumberTest;

public class DecreaseThread extends Thread
{
    private NumberHolder numberHolder;

    public DecreaseThread(NumberHolder numberHolder)
    {
        this.numberHolder = numberHolder;
    }

    @Override
    public void run()
    {
        for (int i = 0; i < 20; ++i)
        {
            // ����һ������ʱ
            try
            {
                Thread.sleep((long) Math.random() * 1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            //System.out.printf("%d: ", i);
            // ���м��ٲ���
            numberHolder.decrease();
        }
    }

}
