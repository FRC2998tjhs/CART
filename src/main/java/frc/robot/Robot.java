// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import frc.robot.Constants.ports;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {

  // up and down buttons
  DigitalInput down_button;
  DigitalInput up_button;

  // limit switches
  DigitalInput up_limit;
  DigitalInput down_limit;

  PWMVictorSPX controller_1;
  PWMVictorSPX controller_2;
  PWMVictorSPX controller_3;

  private Command m_autonomousCommand;

  private static RobotContainer m_robotContainer;

  public void set_speed(float speed) {
    controller_1.set(speed);
    controller_2.set(speed);
    controller_3.set(speed);
  }
  
  public float get_desired_speed() {
    var up_button_status = up_button.get();
    var down_button_status = down_button.get();
    var upper_limit_switch = up_limit.get();
    var lower_limit_switch = down_limit.get();

    if (!upper_limit_switch || !lower_limit_switch) {
      return 0;
    }

    if (!down_button_status) {
      return 1;
    } else if (!up_button_status) {
      return -1;
    }

    return 0;
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
    up_button = new DigitalInput(ports.MOVE_UP);
    down_button = new DigitalInput(ports.MOVE_DOWN);

    up_limit = new DigitalInput(ports.LIMiT_UP);
    down_limit = new DigitalInput(ports.LIMIT_DONW);

    controller_1 = new PWMVictorSPX(ports.MOTOR_PORTS[0]);
    controller_2 = new PWMVictorSPX(ports.MOTOR_PORTS[1]);
    controller_3 = new PWMVictorSPX(ports.MOTOR_PORTS[2]);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

    set_speed(get_desired_speed());

    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
