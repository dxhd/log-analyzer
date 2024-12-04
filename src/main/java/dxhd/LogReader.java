package dxhd;

import java.util.stream.Stream;

public interface LogReader {

    Stream<LogEntry> read(String path);


}
