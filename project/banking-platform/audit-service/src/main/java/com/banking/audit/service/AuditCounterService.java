package com.banking.audit.service;

import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

@Service
public class AuditCounterService {

	private volatile int auditCount = 0;
	private final ReentrantLock lock = new ReentrantLock();

	public void increment() {
		lock.lock();
		try {
			auditCount++;
		} finally {
			lock.unlock();
		}
	}

	public int getCount() {
		return auditCount;
	}
}