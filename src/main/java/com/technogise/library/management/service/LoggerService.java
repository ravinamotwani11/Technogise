/**
* LoggerService
*
* @author  Ravina Motwani
* @version 1.0
* @since   2023-07-23
*/
package com.technogise.library.management.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoggerService {

	/**
	 * 
	 * @param clazz
	 * @return {@link Logger}
	 */
    public Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
