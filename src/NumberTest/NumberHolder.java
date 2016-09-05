package NumberTest;

public class NumberHolder
{
    private int number;

    public synchronized void increase()
    {
        while (0 != number)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        // ��ִ�е�����˵���Ѿ�������
        // ����numberΪ0
        number++;
        System.out.println(number);

        // ֪ͨ�ڵȴ����߳�
        notifyAll();
    }

    public synchronized void decrease()
    {
        while (0 == number)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }

        // ��ִ�е�����˵���Ѿ�������
        // ����number��Ϊ0

        number--;
        System.out.println(number);
        notifyAll();
    }

}
