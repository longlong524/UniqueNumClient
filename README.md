# UniqueNumClient
UniqueNumberService的客户端


# 内部实现
使用threadlocal和LinkedBlockingQueue作为buffer

# 线程安全
多线程安全

# 使用

初始化

    Set<Long> set=new HashSet<Long>();
		LinkedList<ServerBean> sss=new LinkedList<ServerBean>();
		sss.add(new ServerBean(url, scret));

		UniqueNum.init(sss, 10, 1000000);
    
使用

    long tt=UniqueNum.getNext();
