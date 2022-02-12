package com.njuse.seecjvm.runtime;

import lombok.Getter;
import lombok.Setter;

import java.util.EmptyStackException;
import java.util.Stack;

@Setter
@Getter
public class ThreadStack {
    private static int maxSize;
    private Stack<StackFrame> stack;
    // true present frame is new added
    private Stack<Boolean> frameState;
    private int currentSize;

    static {
        //use linux x86_64 default value 256KB
        maxSize = 64 * 1024;
    }

    public ThreadStack() {
        stack = new Stack<>();
        frameState = new Stack<>();
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void pushFrame(StackFrame frame) {
        if (currentSize >= maxSize) {
            throw new StackOverflowError();
        }
        stack.push(frame);
        frameState.push(true);
        currentSize++;
    }

    public void popFrame() {
        if (currentSize == 0) {
            throw new EmptyStackException();
        }
        stack.pop();
        frameState.pop();
        currentSize--;
    }

    public StackFrame getTopFrame() {
        if (currentSize == 0) {
            return null;
        }
        return stack.lastElement();
    }
}
