package service

import com.google.inject.AbstractModule
import com.google.inject.name.Names

class ServiceModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[CategoryServiceInterface]).to(classOf[CategoryService])
  }
}
